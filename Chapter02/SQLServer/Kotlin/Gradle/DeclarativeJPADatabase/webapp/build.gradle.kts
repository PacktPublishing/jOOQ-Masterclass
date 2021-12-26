import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.language.jvm.tasks.ProcessResources
import org.jooq.meta.jaxb.Logging
import org.jooq.meta.jaxb.Property
import org.jooq.meta.jaxb.MatchersTableType
import org.jooq.meta.jaxb.MatcherRule
import org.jooq.meta.jaxb.MatcherTransformType
import org.jooq.meta.jaxb.Matchers

plugins {
    id("application")
    id("org.springframework.boot") version "2.5.7"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("nu.studer.jooq") version "6.0.1"
    id("org.flywaydb.flyway") version "7.7.3"
    kotlin("jvm") version "1.6.0"
    kotlin("plugin.spring") version "1.6.0"
}

val schemaVersion by extra { "1" }

group = "com.classicmodels"
version = "1.0"
java.sourceCompatibility = JavaVersion.VERSION_17

application {
    mainClass.value("com.classicmodels.MainApplicationKt")
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    jooqGenerator("org.jooq.trial-java-8:jooq-meta-extensions-hibernate")
    jooqGenerator("javax.persistence:javax.persistence-api")
    jooqGenerator("javax.validation:validation-api")
    jooqGenerator(project(":entities"))
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation(project(":entities"))
    implementation("com.microsoft.sqlserver:mssql-jdbc")
    implementation("org.flywaydb:flyway-core")
}

tasks {
  "processResources"(ProcessResources::class) {
    filesMatching("application.properties") {
      expand(project.properties)
    }
  }
}

flyway {
    driver = project.properties["driverClassName"].toString()
    url = project.properties["url"].toString()
    user = project.properties["username"].toString()
    password = project.properties["password"].toString()
    locations = arrayOf("filesystem:./../../../../../../db/migration/min/mssql")
    mixed = true
}

jooq {
  version.set(project.properties["jooq"].toString())
  edition.set(nu.studer.gradle.jooq.JooqEdition.TRIAL_JAVA_8)
  
  configurations {
        create("main") {  // name of the jOOQ configuration
            generateSchemaSourceOnCompilation.set(true)  // default (can be omitted)

            jooqConfiguration.apply {
                logging = Logging.WARN
                
                generator.apply {
                    // The default code generator. 
                    // You can override this one, to generate your own code style.
                                     
                    // Supported generators:                                
                    //  - org.jooq.codegen.JavaGenerator
                    //  - org.jooq.codegen.ScalaGenerator
                    //  - org.jooq.codegen.KotlinGenerator
                           
                    // Defaults to org.jooq.codegen.JavaGenerator
                    name = "org.jooq.codegen.KotlinGenerator"
                    
                    database.apply {
                        // Rely on jOOQ JPA Database API
                        name = "org.jooq.meta.extensions.jpa.JPADatabase"
                        
                        // H2 database schema
                        inputSchema = "PUBLIC" 
                        
                        properties.add(
                           // The current versions of jOOQ use Hibernate behind the scenes 
                           // to generate an in-memory H2 database from which to reverse engineer 
                           // jOOQ code. In order to influence Hibernate's schema generation, 
                           // Hibernate specific flags can be passed to MetadataSources. 
                           // All properties that are prefixed with hibernate. 
                           // or javax.persistence. will be passed along to Hibernate. -->
                           Property().withKey("hibernate.physical_naming_strategy").withValue("org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy"))

                        properties.add(   
                           // A comma separated list of Java packages, that contain your entities
                           Property().withKey("packages").withValue("com.classicmodels.entity"))

                        properties.add(   
                           // Whether JPA 2.1 AttributeConverters should be auto-mapped to jOOQ Converters.
                           // Custom <forcedType/> configurations will have a higher priority than these auto-mapped converters. 
                           // This defaults to true.
                           Property().withKey("useAttributeConverters").withValue("true"))

                        properties.add(   
                           // The default schema for unqualified objects:
  
                           // - public: all unqualified objects are located in the PUBLIC (upper ce) schema
                           // - none: all unqualified objects are located in the default schema (dfault) 
                
                           // This configuration can be overridden with the schema mapping feature
                           Property().withKey("unqualifiedSchema").withValue("none"))                        
                                                                        
                        // All elements that are generated from your schema
                        // (A Java regular expression. Use the pipe to separate several expressions)
                        // Watch out for case-sensitivity. Depending on your database, this might be important! 
                        // You can create case-insensitive regular expressions using this syntax: (?i:expr).
                        // Whitespace is ignored and comments are possible.
                        includes = ".*"
                        
                        // Schema version provider
                        schemaVersionProvider = schemaVersion
                    }
                    
                    generate.apply {
                        isDeprecated = false
                        isRecords = true
                        isDaos = true
                        isValidationAnnotations = true
                        isSpringAnnotations = true
                    }
                    
                    strategy.withMatchers(Matchers()
                            .withTables(arrayOf(
                               MatchersTableType()
                                 .withPojoClass(MatcherRule()
                                      .withExpression("Jooq_$0")
                                      .withTransform(MatcherTransformType.PASCAL)),
                               MatchersTableType()
                                 .withDaoClass(MatcherRule()
                                      .withExpression("$0_Repository")
                                      .withTransform(MatcherTransformType.PASCAL))
                            ).toList()))
                            
                    target.apply {
                        packageName = "jooq.generated"
                        directory = "build/generated-sources"
                    }
                }
            }
        }
    }
}

// Configure jOOQ task such that it only executes when something has changed 
// that potentially affects the generated JOOQ sources:
// - the jOOQ configuration has changed (Jdbc, Generator, Strategy, etc.)
// - the classpath used to execute the jOOQ generation tool has changed 
//   (jOOQ library, database driver, strategy classes, etc.)
// - the schema files from which the schema is generated and which is 
//   used by jOOQ to generate the sources have changed (scripts added, modified, etc.)
tasks.named<nu.studer.gradle.jooq.JooqGenerate>("generateJooq") {
    // ensure database schema has been prepared by Flyway before generating the jOOQ sources
    dependsOn("flywayMigrate")

    // declare Flyway migration scripts as inputs on the jOOQ task
    inputs.files(fileTree("${rootDir}/../../../../../../db/migration/min/mssql"))
        .withPropertyName("migrations")
        .withPathSensitivity(PathSensitivity.RELATIVE)

    // make jOOQ task participate in incremental builds and build caching
    allInputsDeclared.set(true)
    outputs.cacheIf { true }
}

java.sourceSets["main"].java {
    srcDir("build/generated-sources/jooq")
    srcDir("src/main/kotlin")
}

tasks.withType<KotlinCompile> {    
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}
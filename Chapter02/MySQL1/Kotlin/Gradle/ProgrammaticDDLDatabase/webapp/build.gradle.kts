import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.language.jvm.tasks.ProcessResources

plugins {
    id("application")
    id("org.springframework.boot") version "2.5.7"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("nu.studer.jooq") version "6.0.1"
    id("org.flywaydb.flyway") version "8.2.0"
    kotlin("jvm") version "1.6.0"
    kotlin("plugin.spring") version "1.6.0"
}

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
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation(project(":jooq-code-generator"))
    implementation("org.jooq:jooq")
    implementation("mysql:mysql-connector-java")
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
    locations = arrayOf("filesystem:./../../../../../../db/migration/ddl/mysql/sql",
                        "filesystem:./../../../../../../db/migration/ddl/mysql/data")
}

jooq {
    version.set(project.properties["jooq"].toString())
    edition.set(nu.studer.gradle.jooq.JooqEdition.OSS) 
}

task("runProgrammaticGenerator", JavaExec::class) {

    dependsOn("flywayMigrate")
    dependsOn(":jooq-code-generator:compileJava")

    val h2jdbc by configurations.creating
    val codegen by configurations.creating   
	val metaext by configurations.creating   

    dependencies {
       codegen("org.jooq:jooq-codegen")
       metaext("org.jooq:jooq-meta-extensions")
       h2jdbc("com.h2database:h2")
    }

    classpath = files(arrayOf(
         "${rootDir}/jooq-code-generator/build/classes/java/main",
         codegen, h2jdbc, metaext
    ))

    mainClass.value("com.classicmodels.jooq.config.JooqConfig")
}

java.sourceSets["main"].java {
    srcDir("build/generated-sources/jooq")
    srcDir("src/main/kotlin")
}

tasks.withType<KotlinCompile> {
    dependsOn("runProgrammaticGenerator")
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}
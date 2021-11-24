package com.classicmodels.jooq.config;

import com.classicmodels.properties.JooqProperties;
import org.jooq.codegen.GenerationTool;
import org.jooq.meta.jaxb.Configuration;
import org.jooq.meta.jaxb.Database;
import org.jooq.meta.jaxb.EmbeddableDefinitionType;
import org.jooq.meta.jaxb.EmbeddableField;
import org.jooq.meta.jaxb.Generate;
import org.jooq.meta.jaxb.Generator;
import org.jooq.meta.jaxb.Jdbc;
import org.jooq.meta.jaxb.Target;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@org.springframework.context.annotation.Configuration
//@SpringBootApplication
public class JooqConfig extends SpringBootServletInitializer {
    
    ////https://coderedirect.com/questions/226178/spring-boot-value-returns-always-null
    
    
private final JooqProperties jooqProperties;

private static JooqProperties instance;       

    public JooqConfig(JooqProperties jooqProperties) {
        this.jooqProperties = jooqProperties;
         instance = jooqProperties;
        System.out.println("dddddddddddddddddddddddddddddddddd:" + jooqProperties+"    "+instance);
    }        
    
    //public static @Qualifier("jooqProperties") JooqProperties jooqProperties;
    
    public static void main(String[] args) throws Exception {
System.out.println("kkkkkkkkkkkkkkkkkkkkkkkkkkkk");
new SpringApplicationBuilder(JooqConfig.class)
            .properties("application-gen.properties").build().run(args);
        if(instance!=null){
        System.out.println("kkkkkkkkkkkkkkkkkkkkkkkkkkkk: " + instance.getWithName());
        }
        Configuration configuration = new Configuration()
                .withJdbc(new Jdbc()
                        .withDriver("org.postgresql.Driver")
                        .withUrl("jdbc:postgresql://localhost:5432/classicmodels")
                        .withUser("postgres")
                        .withPassword("root"))
                .withGenerator(new Generator()
                        .withDatabase(new Database()
                                .withEmbeddables(
                                        new EmbeddableDefinitionType()
                                                .withSchema("public")
                                                .withName("OFFICE_FULL_ADDRESS")
                                                .withComment("The full address of an office")
                                                .withTables(".*\\.office")
                                                .withFields(
                                                        new EmbeddableField()
                                                                .withExpression("CITY"),
                                                        new EmbeddableField()
                                                                // .withName("ADDRESS")
                                                                .withExpression("ADDRESS_LINE_FIRST"),
                                                        new EmbeddableField()
                                                                .withExpression("STATE"),
                                                        new EmbeddableField()
                                                                .withExpression("COUNTRY"),
                                                        new EmbeddableField()
                                                                .withExpression("TERRITORY")))
                                .withName("org.jooq.meta.postgres.PostgresDatabase")
                                .withSchemaVersionProvider("SELECT MAX(version) FROM flyway_schema_history")
                                .withIncludes(".*")
                                .withExcludes("flyway_schema_history")
                                .withInputSchema("public")
                        )
                        .withGenerate(new Generate()
                                .withPojos(true)
                                .withValidationAnnotations(Boolean.TRUE)
                        )
                        .withTarget(new Target()
                                .withPackageName("jooq.generated")
                                .withDirectory(args[0])));

        GenerationTool.generate(configuration);
    }
}

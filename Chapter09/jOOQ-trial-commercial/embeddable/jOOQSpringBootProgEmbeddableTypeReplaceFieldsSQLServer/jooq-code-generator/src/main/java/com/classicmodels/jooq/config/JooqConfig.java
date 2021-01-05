package com.classicmodels.jooq.config;

import org.jooq.codegen.GenerationTool;
import org.jooq.meta.jaxb.Configuration;
import org.jooq.meta.jaxb.Database;
import org.jooq.meta.jaxb.EmbeddableDefinitionType;
import org.jooq.meta.jaxb.EmbeddableField;
import org.jooq.meta.jaxb.Generate;
import org.jooq.meta.jaxb.Generator;
import org.jooq.meta.jaxb.Jdbc;
import org.jooq.meta.jaxb.Target;

public class JooqConfig {

    public static void main(String[] args) throws Exception {

        Configuration configuration = new Configuration()
                .withJdbc(new Jdbc()
                        .withDriver("com.microsoft.sqlserver.jdbc.SQLServerDriver")
                        .withUrl("jdbc:sqlserver://localhost:1433;databaseName=classicmodels")
                        .withUser("sa")
                        .withPassword("root"))
                .withGenerator(new Generator()
                        .withDatabase(new Database()
                                .withEmbeddables(
                                        new EmbeddableDefinitionType()
                                                .withCatalog("classicmodels")
                                                .withSchema("dbo")
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
                                                                .withExpression("TERRITORY"))
                                                .withReplacesFields(true))
                                .withName("org.jooq.meta.sqlserver.SQLServerDatabase")
                                .withSchemaVersionProvider("SELECT MAX(version) FROM flyway_schema_history")
                                .withIncludes(".*")
                                .withExcludes("flyway_schema_history")
                                .withInputCatalog("classicmodels")
                                .withInputSchema("dbo")
                                .withLogSlowQueriesAfterSeconds(20)
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

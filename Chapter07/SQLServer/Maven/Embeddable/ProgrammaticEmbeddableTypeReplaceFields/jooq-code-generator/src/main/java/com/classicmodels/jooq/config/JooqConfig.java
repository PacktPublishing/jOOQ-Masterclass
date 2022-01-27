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
                        .withName("org.jooq.codegen.JavaGenerator")
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
                                .withInputCatalog("classicmodels")
                                .withInputSchema("dbo")
                                .withIncludes(".*")
                                .withExcludes("flyway_schema_history | concatenate | .*_master"
                                        + " | get_.* | refresh_top3_product | sale_price | split_part"
                                        + " | product_of_product_line | top_three_sales_per_employee")
                                .withSchemaVersionProvider("SELECT MAX([version]) FROM [flyway_schema_history]")
                                .withLogSlowQueriesAfterSeconds(20)
                        )
                        .withGenerate(new Generate()
                                .withPojos(true)
                        )
                        .withTarget(new Target()
                                .withPackageName("jooq.generated")
                                .withDirectory(System.getProperty("user.dir").endsWith("webapp")
                                        ? "target/generated-sources"
                                        : "webapp/target/generated-sources")));

        GenerationTool.generate(configuration);
    }
}

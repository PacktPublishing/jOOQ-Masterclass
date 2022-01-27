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
                        .withDriver("org.postgresql.Driver")
                        .withUrl("jdbc:postgresql://localhost:5432/classicmodels")
                        .withUser("postgres")
                        .withPassword("root"))
                .withGenerator(new Generator()
                        .withName("org.jooq.codegen.JavaGenerator")
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
                                .withInputSchema("public")
                                .withIncludes(".*")
                                .withExcludes("flyway_schema_history | akeys | avals | defined | delete.*"
                                        + " | department_topic_arr | dup | employee_office_arr | exist.*"
                                        + " | fetchval | get_.* | ghstore.* | gin.* | hs.* | hstore.*"
                                        + " | isdefined | isexists | make_array | new_salary | populate_record"
                                        + " | sale_price | slice.* | swap | tconvert | update_msrp | postal_code"
                                        + " | evaluation_criteria | rate_type | vat_type | .*_master | each"
                                        + " | skeys | svals | top_three_sales_per_employee | product_of_product_line")
                                .withSchemaVersionProvider("SELECT MAX(\"version\") FROM \"flyway_schema_history\"")
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

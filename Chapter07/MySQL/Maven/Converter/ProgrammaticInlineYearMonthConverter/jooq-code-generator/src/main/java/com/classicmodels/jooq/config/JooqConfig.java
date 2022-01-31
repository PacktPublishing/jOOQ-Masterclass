package com.classicmodels.jooq.config;

import org.jooq.codegen.GenerationTool;
import org.jooq.meta.jaxb.Configuration;
import org.jooq.meta.jaxb.Database;
import org.jooq.meta.jaxb.ForcedType;
import org.jooq.meta.jaxb.Generator;
import org.jooq.meta.jaxb.Jdbc;
import org.jooq.meta.jaxb.Target;

public class JooqConfig {

    public static void main(String[] args) throws Exception {

        Configuration configuration = new Configuration()
                .withJdbc(new Jdbc()
                        .withDriver("com.mysql.cj.jdbc.Driver")
                        .withUrl("jdbc:mysql://localhost:3306/classicmodels?createDatabaseIfNotExist=true")
                        .withUser("root")
                        .withPassword("root"))
                .withGenerator(new Generator()
                        .withName("org.jooq.codegen.JavaGenerator")
                        .withDatabase(new Database()
                                .withForcedTypes(
                                        new ForcedType()
                                                .withUserType("java.time.YearMonth")
                                                .withConverter("org.jooq.Converter.ofNullable(Integer.class, YearMonth.class, "
                                                        + "(Integer t) -> { return YearMonth.of(1970, 1).with(java.time.temporal.ChronoField.PROLEPTIC_MONTH, t); }, "
                                                        + "(YearMonth u) -> { return (int) u.getLong(java.time.temporal.ChronoField.PROLEPTIC_MONTH); })")
                                                .withIncludeExpression("classicmodels\\.customer\\.first_buy_date")
                                                .withIncludeTypes("INT"))
                                .withName("org.jooq.meta.mysql.MySQLDatabase")
                                .withInputSchema("classicmodels")
                                .withIncludes(".*")
                                .withExcludes("flyway_schema_history | sequences"
                                        + " | customer_pgs | refresh_top3_product"
                                        + " | sale_.* | set_.* | get_.* | .*_master")
                                .withSchemaVersionProvider("SELECT MAX(`version`) FROM `flyway_schema_history`")
                                .withLogSlowQueriesAfterSeconds(20)
                        )
                        .withTarget(new Target()
                                .withPackageName("jooq.generated")
                                .withDirectory(System.getProperty("user.dir").endsWith("webapp")
                                        ? "target/generated-sources"
                                        : "webapp/target/generated-sources")));

        GenerationTool.generate(configuration);
    }
}

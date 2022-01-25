package com.classicmodels.jooq.config;

import org.jooq.codegen.GenerationTool;
import org.jooq.meta.jaxb.Configuration;
import org.jooq.meta.jaxb.Database;
import org.jooq.meta.jaxb.ForcedType;
import org.jooq.meta.jaxb.Generate;
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
                        .withDatabase(new Database()
                                .withForcedTypes(
                                        new ForcedType()
                                                .withUserType("java.time.YearMonth")
                                                .withConverter("com.classicmodels.converter.YearMonthConverter")
                                                .withIncludeExpression("classicmodels\\.customer\\.first_buy_date")
                                                .withExcludeTypes(".*\\."))
                                .withName("org.jooq.meta.mysql.MySQLDatabase")
                                .withSchemaVersionProvider("SELECT MAX(version) FROM flyway_schema_history")
                                .withIncludes(".*")
                                .withExcludes("flyway_schema_history")
                                .withInputSchema("classicmodels")
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

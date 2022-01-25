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
                        .withDriver("org.postgresql.Driver")
                        .withUrl("jdbc:postgresql://localhost:5432/classicmodels")
                        .withUser("postgres")
                        .withPassword("root"))
                .withGenerator(new Generator()
                        .withDatabase(new Database()
                                .withForcedTypes(
                                        new ForcedType()
                                                .withUserType("java.net.InetAddress")
                                                .withBinding("com.classicmodels.binding.InetBinding")
                                                .withIncludeExpression(".*\\.dep_net_ipv4")
                                                .withExcludeTypes(".*\\."))
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

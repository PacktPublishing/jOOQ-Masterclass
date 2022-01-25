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
                        .withDriver("oracle.jdbc.driver.OracleDriver")
                        .withUrl("jdbc:oracle:thin:@localhost:1521:xe")
                        .withUser("SYSTEM")
                        .withPassword("root"))
                .withGenerator(new Generator()
                        .withDatabase(new Database()
                                .withForcedTypes(
                                        new ForcedType()
                                                .withName("BOOLEAN")
                                                .withIncludeExpression("SYSTEM\\.SALE\\.HOT")
                                                .withIncludeTypes("NUMBER\\(1,\\s*0\\)")
                                )
                                .withName("org.jooq.meta.oracle.OracleDatabase")
                                .withSchemaVersionProvider("SELECT MAX(\"version\") FROM \"SYSTEM\".\"flyway_schema_history\"")
                                .withIncludes(".*")
                                .withExcludes("flyway_schema_history | HELP | ROLLING.* | LOGMNR.* | PRODUCT_PRIVS"
                                        + " | AQ.* | MVIEW.* | LOGMNRC.* | LOGMNRGGC.* | LOGMNRP.*"
                                        + " | LOGMNRT.* | OL.* | REDO.* | SCHEDULER.* | SQLPLUS.* | LOGSTDBY.*")
                                .withInputSchema("SYSTEM")
                                .withLogSlowResultsAfterSeconds(20)
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

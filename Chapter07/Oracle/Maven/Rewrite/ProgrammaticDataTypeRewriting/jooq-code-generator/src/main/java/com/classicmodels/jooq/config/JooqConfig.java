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
                        .withDriver("oracle.jdbc.driver.OracleDriver")
                        .withUrl("jdbc:oracle:thin:@localhost:1521:xe")
                        .withUser("CLASSICMODELS")
                        .withPassword("root"))
                .withGenerator(new Generator()
                        .withName("org.jooq.codegen.JavaGenerator")
                        .withDatabase(new Database()
                                .withForcedTypes(
                                        new ForcedType()
                                                .withName("BOOLEAN")
                                                .withIncludeExpression("CLASSICMODELS\\.SALE\\.HOT")
                                                .withIncludeTypes("CHAR\\(1\\)"))
                                .withName("org.jooq.meta.oracle.OracleDatabase")                                
                                .withInputSchema("CLASSICMODELS")
                                .withIncludes(".*")
                                .withExcludes("flyway_schema_history | DEPARTMENT_PKG | GET_.*"
                                        + " | CARD_COMMISSION | PRODUCT_OF_PRODUCT_LINE"
                                        + " | REFRESH_TOP3_PRODUCT | SALE_PRICE | SECOND_MAX"
                                        + " | SET_COUNTER | SWAP | TOP_THREE_SALES_PER_EMPLOYEE"
                                        + " | EVALUATION_CRITERIA | SECOND_MAX_IMPL | TABLE_.*_OBJ"
                                        + " | .*_MASTER | BGT | .*_ARR | TABLE_POPL | TABLE_RES")                                
                                .withSchemaVersionProvider("SELECT MAX(\"version\") FROM \"flyway_schema_history\"")
                        )
                        .withTarget(new Target()
                                .withPackageName("jooq.generated")
                                .withDirectory(System.getProperty("user.dir").endsWith("webapp")
                                        ? "target/generated-sources"
                                        : "webapp/target/generated-sources")));

        GenerationTool.generate(configuration);
    }
}

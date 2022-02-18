package com.classicmodels.jooq.config;

import org.jooq.codegen.GenerationTool;
import org.jooq.meta.jaxb.Configuration;
import org.jooq.meta.jaxb.Database;
import org.jooq.meta.jaxb.Generate;
import org.jooq.meta.jaxb.Generator;
import org.jooq.meta.jaxb.Jdbc;
import org.jooq.meta.jaxb.MatcherRule;
import org.jooq.meta.jaxb.MatcherTransformType;
import org.jooq.meta.jaxb.Matchers;
import org.jooq.meta.jaxb.MatchersTableType;
import org.jooq.meta.jaxb.Property;
import org.jooq.meta.jaxb.Strategy;
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
                                .withName("org.jooq.meta.xml.XMLDatabase")       
                                .withProperties(
                                        new Property().withKey("dialect").withValue("ORACLE"),
                                        new Property().withKey("xmlFile").withValue("./../../../../../db/migration/xml/oracle/sql.xml")
                                )
                                .withIncludes(".*")
                                .withExcludes("flyway_schema_history | DEPARTMENT_PKG | GET_.*"
                                        + " | CARD_COMMISSION | PRODUCT_OF_PRODUCT_LINE"
                                        + " | REFRESH_TOP3_PRODUCT | SALE_PRICE | SECOND_MAX"
                                        + " | SET_COUNTER | SWAP | TOP_THREE_SALES_PER_EMPLOYEE"
                                        + " | EVALUATION_CRITERIA | SECOND_MAX_IMPL | TABLE_.*_OBJ"
                                        + " | .*_MASTER | BGT | .*_ARR | TABLE_POPL | TABLE_RES")                                
                                .withSchemaVersionProvider("com.classicmodels.jooq.config.MySchemaVersionProvider")                                
                        )
                        .withGenerate(new Generate()
                                .withDaos(true)
                                .withValidationAnnotations(Boolean.TRUE)
                                .withSpringAnnotations(Boolean.TRUE)
                        )
                        .withStrategy(new Strategy()
                                .withMatchers(new Matchers()
                                        .withTables(new MatchersTableType()
                                                .withPojoClass(new MatcherRule()
                                                        .withExpression("Jooq_$0")
                                                        .withTransform(MatcherTransformType.PASCAL))
                                                .withDaoClass(new MatcherRule()
                                                        .withExpression("$0_Repository")
                                                        .withTransform(MatcherTransformType.PASCAL))))
                        )
                        .withTarget(new Target()
                                .withPackageName("jooq.generated")
                                .withDirectory(System.getProperty("user.dir").endsWith("webapp")
                                        ? "target/generated-sources"
                                        : "webapp/target/generated-sources")));

        GenerationTool.generate(configuration);
    }
}

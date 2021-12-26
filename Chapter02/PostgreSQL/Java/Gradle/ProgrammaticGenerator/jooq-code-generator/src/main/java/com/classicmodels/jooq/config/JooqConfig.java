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
import org.jooq.meta.jaxb.Strategy;
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
                                .withDirectory(System.getProperty("user.dir") 
								+ "/../webapp/build/generated-sources")));

        GenerationTool.generate(configuration);
    }
}

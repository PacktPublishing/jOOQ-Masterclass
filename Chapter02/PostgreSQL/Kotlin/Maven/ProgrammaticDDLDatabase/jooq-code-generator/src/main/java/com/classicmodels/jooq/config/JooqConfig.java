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
import org.jooq.meta.jaxb.Property;

public class JooqConfig {

    public static void main(String[] args) throws Exception {

        Configuration configuration = new Configuration()
                .withGenerator(new Generator()
                        .withName("org.jooq.codegen.KotlinGenerator")
                        .withDatabase(new Database()
                                .withName("org.jooq.meta.extensions.ddl.DDLDatabase")
                                .withInputSchema("PUBLIC")  
                                .withProperties(
                                        new Property().withKey("scripts").withValue(
                                                System.getProperty("user.dir").endsWith("webapp")
                                                ? "./../../../../../../db/migration/ddl/postgresql/sql"
                                                : "./../../../../../db/migration/ddl/postgresql/sql"),
                                        new Property().withKey("sort").withValue("flyway"),
                                        new Property().withKey("unqualifiedSchema").withValue("none"),
                                        new Property().withKey("defaultNameCase").withValue("as_is"))
                                .withIncludes(".*")
                                .withExcludes("flyway_schema_history | akeys | avals | defined | delete.*"
                                      + " | department_topic_arr | dup | employee_office_arr | exist.*"
                                      + " | fetchval | get_.* | ghstore.* | gin.* | hs.* | hstore.*"
                                      + " | isdefined | isexists | make_array | new_salary | populate_record"
                                      + " | sale_price | slice.* | swap | tconvert | update_msrp | postal_code"
                                      + " | evaluation_criteria | rate_type | vat_type | .*_master | each"
                                      + " | skeys | svals | top_three_sales_per_employee | product_of_product_line")
                                .withSchemaVersionProvider("com.classicmodels.jooq.config.MySchemaVersionProvider")
                        )
                        .withGenerate(new Generate()
                                .withDaos(true)
                                .withValidationAnnotations(Boolean.TRUE)
                                .withSpringAnnotations(Boolean.TRUE)
                                .withDeprecationOnUnknownTypes(Boolean.FALSE)
                                .withPojosAsKotlinDataClasses(Boolean.TRUE)
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

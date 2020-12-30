package com.classicmodels.jooq.config;

import com.classicmodels.properties.DataSourceProperties;
import com.classicmodels.properties.JooqProperties;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.jooq.codegen.GenerationTool;
import org.jooq.meta.jaxb.Configuration;
import org.jooq.meta.jaxb.Database;
import org.jooq.meta.jaxb.ForcedType;
import org.jooq.meta.jaxb.Generate;
import org.jooq.meta.jaxb.Generator;
import org.jooq.meta.jaxb.Jdbc;
import org.jooq.meta.jaxb.MatcherRule;
import org.jooq.meta.jaxb.MatcherTransformType;
import org.jooq.meta.jaxb.Matchers;
import org.jooq.meta.jaxb.MatchersTableType;
import org.jooq.meta.jaxb.Strategy;
import org.jooq.meta.jaxb.Target;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;

@org.springframework.context.annotation.Configuration
public class JooqConfig {

    private final DataSource ds;

    public JooqConfig(DataSource ds) {
        this.ds = ds;
    }

    @Bean(name = "dsProperties")
    public DataSourceProperties dsProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "jooqProperties")
    public JooqProperties jooqProperties() {
        return new JooqProperties();
    }

    @FlywayDataSource
    @Bean(initMethod = "migrate", name = "dsFlyway")
    public Flyway primaryFlyway() {

        return Flyway.configure()
                .dataSource(ds)
                .load();
    }

    @Bean
    @DependsOn("dsFlyway")
    public Configuration jooqCodeGen(
            @Qualifier("dsProperties") DataSourceProperties dsProperties,
            @Qualifier("jooqProperties") JooqProperties jooqProperties) throws Exception {

        Configuration configuration = new Configuration()
                .withJdbc(new Jdbc()
                        .withDriver(dsProperties.getDriverClassName())
                        .withUrl(dsProperties.getUrl())
                        .withUser(dsProperties.getUsername())
                        .withPassword(dsProperties.getPassword()))
                .withGenerator(new Generator()
                        .withDatabase(new Database()
                                .withForcedTypes(
                                        new ForcedType()
                                                .withUserType(jooqProperties.getWithUserType1())
                                                .withConverter(jooqProperties.getWithConverter1())
                                                .withIncludeExpression(jooqProperties.getWithIncludeExpression1())
                                                .withExcludeTypes(jooqProperties.getWithIncludeTypes1()),
                                        new ForcedType()
                                                .withUserType(jooqProperties.getWithUserType2())
                                                .withConverter(jooqProperties.getWithConverter2())
                                                .withIncludeExpression(jooqProperties.getWithIncludeExpression2())
                                                .withExcludeTypes(jooqProperties.getWithIncludeTypes2()),
                                        new ForcedType()
                                                .withUserType(jooqProperties.getWithUserType3())
                                                .withConverter(jooqProperties.getWithConverter3())
                                                .withIncludeExpression(jooqProperties.getWithIncludeExpression3())
                                                .withExcludeTypes(jooqProperties.getWithIncludeTypes3()))
                                .withName(jooqProperties.getWithName())
                                .withSchemaVersionProvider(jooqProperties.getWithSchemaVersionProvider())
                                .withIncludes(jooqProperties.getWithIncludes())
                                .withExcludes(jooqProperties.getWithExcludes())
                                .withInputCatalog(jooqProperties.getWithInputCatalog())
                                .withInputSchema(jooqProperties.getWithInputSchema()))                        
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
                                .withPackageName(jooqProperties.getWithPackageName())
                                .withDirectory(jooqProperties.getWithDirectory())));

        GenerationTool.generate(configuration);

        return configuration;
    }

}

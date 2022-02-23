package com.classicmodels.jooq.config;

import javax.sql.DataSource;
import org.jooq.ConnectionProvider;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.ThreadLocalTransactionProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jooq.JooqProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JooqConfig {

    @Bean
    @ConditionalOnMissingBean(org.jooq.Configuration.class)
    public DefaultConfiguration jooqConfiguration(
            JooqProperties properties, DataSource ds) {

        final DefaultConfiguration defaultConfig = new DefaultConfiguration();
        final ConnectionProvider cp = new DataSourceConnectionProvider(ds);

        defaultConfig
                .set(properties.determineSqlDialect(ds))
                .set(new ThreadLocalTransactionProvider(cp, true));

        /* or, as a derived configuration
        final org.jooq.Configuration derivedConfig = defaultConfig
                .derive(properties.determineSqlDialect(ds))
                .derive(new ThreadLocalTransactionProvider(cp, true));
         */
        
        return defaultConfig;
    }
}

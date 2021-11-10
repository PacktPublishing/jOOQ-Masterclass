package com.classicmodels.jooq.config;

import com.classicmodels.providers.MyTransactionProvider;
import javax.sql.DataSource;
import org.jooq.ConnectionProvider;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jooq.JooqProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class JooqConfig {

    @Bean
    @ConditionalOnMissingBean(org.jooq.Configuration.class)
    public DefaultConfiguration jooqConfiguration(
            JooqProperties properties, DataSource ds, PlatformTransactionManager txManager) {

        final ConnectionProvider cp = new DataSourceConnectionProvider(ds);
        final DefaultConfiguration defaultConfig = new DefaultConfiguration();

        defaultConfig
                .set(cp)
                .set(properties.determineSqlDialect(ds))
                .set(new MyTransactionProvider(txManager));

        /* or, as a derived configuration
        final org.jooq.Configuration derivedConfig
                = defaultConfig.derive(cp)
                        .derive(properties.determineSqlDialect(ds))
                        .derive(new MyTransactionProvider(txManager));
         */
        return defaultConfig;
    }
}

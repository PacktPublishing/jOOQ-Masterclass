package com.classicmodels.jooq.config;

import javax.sql.DataSource;
import org.jooq.ConnectionProvider;
import org.jooq.TransactionProvider;
import org.jooq.conf.RenderKeywordCase;
import org.jooq.conf.Settings;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jooq.JooqProperties;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class JooqConfig {       

    @Bean
    @ConditionalOnMissingBean(org.jooq.Configuration.class)
    public DefaultConfiguration jooqConfiguration(
            JooqProperties properties, DataSource ds, 
            ConnectionProvider cp, TransactionProvider tp) {
       
        final DefaultConfiguration defaultConfig = new DefaultConfiguration();

        defaultConfig               
                .set(cp)                                  // must have                
                .set(properties.determineSqlDialect(ds))  // must have
                .set(tp)                                  // must have only if you want to use SpringTransactionProvider in jOOQ transactions
                .set(new Settings().withRenderKeywordCase(RenderKeywordCase.UPPER)); // optional
                // more configs ...

        /* or, as a derived configuration
        final org.jooq.Configuration derivedConfig = defaultConfig
                .derive(cp)
                .derive(tp)
                .derive(properties.determineSqlDialect(ds));
                // more configs ...
         */
        
        return defaultConfig;
    }
}

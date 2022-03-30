package com.classicmodels.jooq.config;

import com.classicmodels.provider.MyRecordMapperProvider;
import javax.sql.DataSource;
import org.jooq.ConnectionProvider;
import org.jooq.TransactionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jooq.JooqProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JooqConfig {

    @Bean
    @ConditionalOnMissingBean(org.jooq.Configuration.class)
    public DefaultConfiguration jooqConfiguration(
            JooqProperties properties, DataSource ds, 
            ConnectionProvider cp, TransactionProvider tp) {
       
        final DefaultConfiguration defaultConfig = new DefaultConfiguration();

        defaultConfig               
                .set(cp)
                .set(tp)
                .set(properties.determineSqlDialect(ds))
                .set(new MyRecordMapperProvider());

        /* or, as a derived configuration
        final org.jooq.Configuration derivedConfig = defaultConfig
                .derive(cp)
                .derive(tp)
                .derive(properties.determineSqlDialect(ds))
                .derive(new MyRecordMapperProvider());
         */
        
        return defaultConfig;
    }
}

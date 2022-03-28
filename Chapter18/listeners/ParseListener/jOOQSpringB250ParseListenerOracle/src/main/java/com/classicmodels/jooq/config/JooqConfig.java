package com.classicmodels.jooq.config;

import com.classicmodels.listener.MyParseListener;
import javax.sql.DataSource;
import org.jooq.ConnectionProvider;
import org.jooq.TransactionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultParseListenerProvider;
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

        final DefaultConfiguration defaultConfig
                = new DefaultConfiguration();

        defaultConfig
                .set(cp)
                .set(properties.determineSqlDialect(ds))
                .set(tp)
                .set(new DefaultParseListenerProvider(new MyParseListener())); 

                 // or, 
                 // defaultConfig.set(new DefaultParseListenerProvider(new MyParseListener1()),
                 //                   new DefaultParseListenerProvider(new MyParseListener2()),
                 //                   ...)
                 // defaultConfig.set(new MyParseListener())
                 // defaultConfig.set(new MyParseListener1(), new MyParseListener2(), ...); 
                 // defaultConfig.setParseListener(new MyParseListener1(), new MyParseListener2(), ...);

        return defaultConfig;
    }
}

package com.classicmodels.jooq.config;

import com.classicmodels.listener.MyExecuteListener;
import javax.sql.DataSource;
import org.jooq.ConnectionProvider;
import org.jooq.TransactionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultExecuteListenerProvider;
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
                .set(new DefaultExecuteListenerProvider(new MyExecuteListener())); 
 
                 // or,               
                 // defaultConfig.set(new DefaultExecuteListenerProvider(new MyExecuteListener1(),
                 //                   new DefaultExecuteListenerProvider(new MyExecuteListener2()),
                 //                   ...)
                 // defaultConfig.set(new MyExecuteListener())
                 // defaultConfig.set(new MyExecuteListener1(), new MyExecuteListener2(), ...); 
                 // defaultConfig.setExecuteListener(new MyExecuteListener1(), new MyExecuteListener2(), ...);

        return defaultConfig;
    }
}

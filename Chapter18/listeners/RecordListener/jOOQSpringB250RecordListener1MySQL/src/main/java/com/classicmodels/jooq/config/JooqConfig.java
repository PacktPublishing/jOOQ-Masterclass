package com.classicmodels.jooq.config;

import com.classicmodels.listener.MyRecordListener;
import javax.sql.DataSource;
import org.jooq.ConnectionProvider;
import org.jooq.TransactionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultRecordListenerProvider;
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
                .set(new DefaultRecordListenerProvider(new MyRecordListener())); 

        // or
        // defaultConfig.set(new DefaultRecordListenerProvider(new MyRecordListener1()),
        //                   new DefaultRecordListenerProvider(new MyRecordListener2()),
        //                   ...);        
        // defaultConfig.setRecordListener(new MyRecordListener1(), new MyRecordListener2(), ...);
        // defaultConfig.set(new MyRecordListener());
        // defaultConfig.set(new MyRecordListener1(), new MyRecordListener2(), ...);

        return defaultConfig;
    }
}

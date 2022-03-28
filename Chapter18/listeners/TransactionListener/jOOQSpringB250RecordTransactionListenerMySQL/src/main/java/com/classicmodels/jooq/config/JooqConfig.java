package com.classicmodels.jooq.config;

import com.classicmodels.listener.MyRecordListener;
import com.classicmodels.listener.MyTransactionListener;
import javax.sql.DataSource;
import org.jooq.ConnectionProvider;
import org.jooq.TransactionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultRecordListenerProvider;
import org.jooq.impl.DefaultTransactionListenerProvider;
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
                .set(new DefaultRecordListenerProvider(new MyRecordListener()))
                .set(new DefaultTransactionListenerProvider(new MyTransactionListener())); 
        // or
        // defaultConfig.setRecordListener(new MyRecordListener());
        // defaultConfig.setTransactionListener(new MyTransactionListener());
        // defaultConfig.set(new MyRecordListener());
        // defaultConfig.set(new MyTransactionListener());
        
        return defaultConfig;
    }
}

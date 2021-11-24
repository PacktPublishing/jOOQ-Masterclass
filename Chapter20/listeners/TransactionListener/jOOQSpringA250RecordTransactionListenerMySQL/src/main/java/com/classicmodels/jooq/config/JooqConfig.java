package com.classicmodels.jooq.config;

import com.classicmodels.listener.MyRecordListener;
import com.classicmodels.listener.MyTransactionListener;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultRecordListenerProvider;
import org.jooq.impl.DefaultTransactionListenerProvider;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JooqConfig implements DefaultConfigurationCustomizer {

    @Override
    public void customize(DefaultConfiguration configuration) {

        configuration.set(new DefaultRecordListenerProvider(new MyRecordListener()));
        configuration.set(new DefaultTransactionListenerProvider(new MyTransactionListener()));
                
        // or, 
        // configuration.set(new MyRecordListener());
        // configuration.set(new MyTransactionListener());
        // configuration.setRecordListener(new MyRecordListener());
        // configuration.setTransactionListener(new MyTransactionListener());
    }

}
package com.classicmodels.jooq.config;

import com.classicmodels.listener.MyRecordListener;
import com.classicmodels.listener.MyTransactionListener;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JooqConfig implements DefaultConfigurationCustomizer {

    @Override
    public void customize(DefaultConfiguration configuration) {

        configuration.set(new MyRecordListener());
        configuration.set(new MyTransactionListener());
    }

}
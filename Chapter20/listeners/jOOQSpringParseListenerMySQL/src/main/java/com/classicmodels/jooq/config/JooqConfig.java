package com.classicmodels.jooq.config;

import com.classicmodels.listener.MyParseListener;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultParseListenerProvider;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JooqConfig implements DefaultConfigurationCustomizer {

    @Override
    public void customize(DefaultConfiguration configuration) {

        configuration.set(new DefaultParseListenerProvider(new MyParseListener()));
    }
}

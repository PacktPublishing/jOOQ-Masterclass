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
        
        // or
        // defaultConfig.set(new DefaultParseListenerProvider(new MyParseListener1()),
        //                   new DefaultParseListenerProvider(new MyParseListener2()),
        //                   ...)
        // configuration.set(new MyParseListener());
        // configuration.set(new MyParseListener1(), new MyParseListener2(), ...);
        // configuration.setParseListener(new MyParseListener1(), new MyParseListener2());
    }
}

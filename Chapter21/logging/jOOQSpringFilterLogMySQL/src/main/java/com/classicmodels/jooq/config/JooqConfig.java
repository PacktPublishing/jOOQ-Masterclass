package com.classicmodels.jooq.config;

import com.classicmodels.listener.MyLoggerListener;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JooqConfig implements DefaultConfigurationCustomizer {

    @Override
    public void customize(DefaultConfiguration configuration) {

        configuration.settings().withExecuteLogging(Boolean.FALSE); // suppress jOOQ logging
        configuration.set(new DefaultExecuteListenerProvider(new MyLoggerListener()));                              
    }

}

package com.classicmodels.jooq.config;

import com.classicmodels.listener.MyExecuteListener;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JooqConfig implements DefaultConfigurationCustomizer {

    @Override
    public void customize(DefaultConfiguration configuration) {

        configuration.set(new DefaultExecuteListenerProvider(new MyExecuteListener()));
    }

}

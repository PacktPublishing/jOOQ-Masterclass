package com.classicmodels.jooq.config;

import com.classicmodels.listener.MyVisitListener;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultVisitListenerProvider;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JooqConfig implements DefaultConfigurationCustomizer {

    @Override
    public void customize(DefaultConfiguration configuration) {

        configuration.set(new DefaultVisitListenerProvider(new MyVisitListener()));
        
        // or
        // configuration.set(new MyVisitListener());
        // // configuration.set(new MyVisitListener1(), new MyVisitListener2(), ...);
    }

}

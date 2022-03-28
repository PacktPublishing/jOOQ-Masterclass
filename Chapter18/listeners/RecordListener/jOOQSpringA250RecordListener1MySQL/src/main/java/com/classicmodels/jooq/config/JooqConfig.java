package com.classicmodels.jooq.config;

import com.classicmodels.listener.MyRecordListener;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultRecordListenerProvider;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JooqConfig implements DefaultConfigurationCustomizer {

    @Override
    public void customize(DefaultConfiguration configuration) {

        configuration.set(new DefaultRecordListenerProvider(new MyRecordListener()));               
        
        // or
        // configuration.set(new DefaultRecordListenerProvider(new MyRecordListener1()),
        //                   new DefaultRecordListenerProvider(new MyRecordListener2()),
        //                   ...);        
        // configuration.setRecordListener(new MyRecordListener1(), new MyRecordListener2(), ...);
        // configuration.set(new MyRecordListener());
        // configuration.set(new MyRecordListener1(), new MyRecordListener2(), ...);
    }

}

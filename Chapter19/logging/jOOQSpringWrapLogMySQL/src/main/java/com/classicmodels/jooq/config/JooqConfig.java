package com.classicmodels.jooq.config;

import com.classicmodels.listener.MyLoggerListener;
import org.jooq.conf.InvocationOrder;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JooqConfig implements DefaultConfigurationCustomizer {

    @Override
    public void customize(DefaultConfiguration configuration) {

        configuration.set(new DefaultExecuteListenerProvider(new MyLoggerListener()));     
        
        // this is useful only for the first logged query, to log the query before the jOOQ logo
        configuration.settings().withExecuteListenerEndInvocationOrder(InvocationOrder.REVERSE); 
    }

}

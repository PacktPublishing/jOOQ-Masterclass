package com.classicmodels.jooq.config;

import com.classicmodels.listener.MyVisitListener;
import javax.sql.DataSource;
import org.jooq.ConnectionProvider;
import org.jooq.TransactionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultVisitListenerProvider;
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
                .set(new DefaultVisitListenerProvider(new MyVisitListener())); 

        // or
        // defaultConfig.set(new DefaultVisitListenerProvider(new MyVisitListener1()),
        //                   new DefaultVisitListenerProvider(new MyVisitListener2()),
        //                    ...);                        
        // defaultConfig.set(new MyVisitListener());
        // defaultConfig.set(new MyVisitListener1(), new MyVisitListener2(), ...);
        // defaultConfig.setVisitListener(new MyVisitListener1(),new MyVisitListener2(), ...);

        return defaultConfig;
    }
}

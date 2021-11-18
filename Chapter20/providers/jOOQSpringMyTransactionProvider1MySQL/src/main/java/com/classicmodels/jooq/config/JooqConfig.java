package com.classicmodels.jooq.config;

import com.classicmodels.providers.MyTransactionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.transaction.PlatformTransactionManager;

@org.springframework.context.annotation.Configuration
public class JooqConfig implements DefaultConfigurationCustomizer {

    private final PlatformTransactionManager txManager;

    public JooqConfig(PlatformTransactionManager txManager) {
        this.txManager = txManager;
    }        

    @Override
    public void customize(DefaultConfiguration configuration) {
        
        configuration.set(new MyTransactionProvider(txManager));
    }
}

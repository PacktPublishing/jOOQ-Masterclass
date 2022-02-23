package com.classicmodels.jooq.settings;

import org.jooq.conf.Settings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JooqSetting {

    @Bean
    public Settings jooqSettings() {        
        return new Settings()                
                .withExecuteWithOptimisticLocking(true);
                // .withExecuteWithOptimisticLockingExcludeUnversioned(true) // disable optimistic locking, unless versioned (numeric/timestamp) is not used
    }
}

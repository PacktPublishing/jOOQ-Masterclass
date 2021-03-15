package com.classicmodels.jooq.settings;

import org.jooq.conf.Settings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JooqSetting {

    @Bean
    public Settings jooqSettings() {
        return new Settings()       
                .withUpdateRecordVersion(true) // this is default, so it can be omitted                
                .withExecuteWithOptimisticLocking(true)
                .withExecuteWithOptimisticLockingExcludeUnversioned(true);                
    }
}

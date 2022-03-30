package com.classicmodels.jooq.config;

import org.jooq.conf.Settings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JooqConfig {

    @Bean
    public Settings jooqSettings() {
        return new Settings()
                .withRenderSchema(Boolean.FALSE); // this is a setting
        // more settings added here
    }
}

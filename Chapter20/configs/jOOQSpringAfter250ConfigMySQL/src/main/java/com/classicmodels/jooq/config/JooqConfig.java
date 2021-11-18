package com.classicmodels.jooq.config;

import org.jooq.conf.RenderKeywordCase;
import org.jooq.conf.Settings;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;

@org.springframework.context.annotation.Configuration
public class JooqConfig implements DefaultConfigurationCustomizer {

    @Override
    public void customize(DefaultConfiguration configuration) {

        configuration.set(new Settings().withRenderKeywordCase(RenderKeywordCase.UPPER)); // optional
        // more configs ...
    }
}

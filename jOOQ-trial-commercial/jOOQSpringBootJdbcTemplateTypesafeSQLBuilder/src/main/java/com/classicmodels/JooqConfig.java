package com.classicmodels;

import org.jooq.conf.RenderQuotedNames;
import org.jooq.conf.Settings;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
public class JooqConfig {


    @Bean         
    public Settings jooqSettings() {         
        return new Settings()  // or via withRenderMapping
                .withRenderCatalog(Boolean.FALSE)                
                .withRenderQuotedNames(RenderQuotedNames.NEVER)
                .withRenderSchema(Boolean.FALSE);                
    }

}

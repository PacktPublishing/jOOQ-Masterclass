package com.classicmodels.config;

import io.r2dbc.spi.ConnectionFactory;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JooqReactiveConfig {

    public final ConnectionFactory cf;

    public JooqReactiveConfig(ConnectionFactory cf) {
        this.cf = cf;
    }

    @Bean
    public DSLContext jOOQDSLContext() {

        return DSL.using(cf).dsl();
    }

}

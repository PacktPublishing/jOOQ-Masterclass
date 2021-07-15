package com.classicmodels;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JooqConfig {

    public final ConnectionFactory cfi;

    public JooqConfig(ConnectionFactory cfi) {
        this.cfi = cfi;
    }

    @Bean
    public DSLContext jOOQDSLContext() {
System.out.println("qqqqqqqqqqqqqqq="+cfi);
/*
        ConnectionFactoryOptions options = ConnectionFactoryOptions.builder()
                .option(ConnectionFactoryOptions.DRIVER, "postgresql")
                .option(ConnectionFactoryOptions.PROTOCOL, "r2dbc")
                .option(ConnectionFactoryOptions.USER, "postgres")
                .option(ConnectionFactoryOptions.PASSWORD, "root")
                .option(ConnectionFactoryOptions.HOST, "localhost")
                .option(ConnectionFactoryOptions.PORT, 5432)
                .option(ConnectionFactoryOptions.DATABASE, "classicmodels")
                .build();

        ConnectionFactory cf = ConnectionFactories.get(options);
*/
        return DSL.using(cfi).dsl();
    }

}

package com.classicmodels.jooq.config;

import javax.sql.DataSource;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.ThreadLocalTransactionProvider;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JooqConfig implements DefaultConfigurationCustomizer {

    private final DataSource ds;

    public JooqConfig(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public void customize(DefaultConfiguration configuration) {

        configuration.set(new ThreadLocalTransactionProvider(
                new DataSourceConnectionProvider(ds), true));

    }
}

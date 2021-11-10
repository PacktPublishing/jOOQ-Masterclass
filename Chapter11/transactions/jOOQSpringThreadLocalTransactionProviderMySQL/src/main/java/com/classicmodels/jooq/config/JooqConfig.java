package com.classicmodels.jooq.config;

import com.zaxxer.hikari.HikariDataSource;
import org.jooq.ConnectionProvider;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.ThreadLocalTransactionProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JooqConfig {

    @Bean(name = "myDSLContext")
    public DSLContext myDSLContext(HikariDataSource ds) {

        final ConnectionProvider cp = new DataSourceConnectionProvider(ds);
        final org.jooq.Configuration configuration = new DefaultConfiguration()
                .set(cp)
                .set(SQLDialect.MYSQL)
                .set(new ThreadLocalTransactionProvider(cp, true));
        
        return DSL.using(configuration);
    }
}

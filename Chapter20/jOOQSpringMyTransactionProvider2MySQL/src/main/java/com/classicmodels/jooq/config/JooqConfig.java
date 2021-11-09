package com.classicmodels.jooq.config;

import com.classicmodels.providers.MyTransactionProvider;
import com.zaxxer.hikari.HikariDataSource;
import org.jooq.ConnectionProvider;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class JooqConfig {

    @Bean(name = "myDSLContext")
    public DSLContext myDSLContext(HikariDataSource ds, PlatformTransactionManager txManager) {

        final ConnectionProvider cp = new DataSourceConnectionProvider(ds);
        final org.jooq.Configuration configuration = new DefaultConfiguration()
                .set(cp)
                .set(SQLDialect.MYSQL)
                .set(new MyTransactionProvider(txManager));

        return DSL.using(configuration);
    }
}

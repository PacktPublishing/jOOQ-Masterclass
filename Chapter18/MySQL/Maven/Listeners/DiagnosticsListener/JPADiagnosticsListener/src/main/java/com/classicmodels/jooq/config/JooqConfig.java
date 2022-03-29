package com.classicmodels.jooq.config;

import com.classicmodels.listener.MyDiagnosticsListener;
import java.sql.Connection;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultDiagnosticsListenerProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

@org.springframework.context.annotation.Configuration
public class JooqConfig {

    @Primary
    @Bean(name = "configDS")
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties firstDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean(name = "singleConnectionDataSource")
    public SingleConnectionDataSource singleConnectionDataSource(
            @Qualifier("configDS") DataSourceProperties properties) {

        Connection conn = DSL.using(properties.getUrl(), properties.getUsername(), properties.getPassword())
                .configuration()
                .set(new DefaultDiagnosticsListenerProvider(new MyDiagnosticsListener()))
                .dsl()
                .diagnosticsConnection();

        return new SingleConnectionDataSource(conn, true);
    }
}

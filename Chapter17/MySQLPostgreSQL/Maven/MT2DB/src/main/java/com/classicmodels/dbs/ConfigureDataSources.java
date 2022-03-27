package com.classicmodels.dbs;

import com.zaxxer.hikari.HikariDataSource;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ConfigureDataSources {
    
    @Primary
    @Bean(name = "configMySql")
    @ConfigurationProperties("mysql.spring.datasource")
    public DataSourceProperties firstDataSourceProperties() {
        return new DataSourceProperties();
    }
    
    @Primary
    @Bean(name = "dataSourceMySql")
    @ConfigurationProperties("mysql.spring.datasource")
    public HikariDataSource firstDataSource(@Qualifier("configMySql") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class)
                .build();
    }
    
    @Bean(name="mysqlDSLContext")
    public DSLContext mysqlDSLContext(@Qualifier("configMySql") DataSourceProperties properties) {
        return DSL.using(
                properties.getUrl(), properties.getUsername(), properties.getPassword());
    }
    
    @Bean(name = "configPostgreSql")
    @ConfigurationProperties("postgresql.spring.datasource")
    public DataSourceProperties secondDataSourceProperties() {
        return new DataSourceProperties();
    }
    
     @Bean(name = "dataSourcePostgreSql")
    @ConfigurationProperties("postgresql.spring.datasource")
    public HikariDataSource secondDataSource(@Qualifier("configPostgreSql") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class)
                .build();
    }        
    
    @Bean(name="postgresqlDSLContext")
    public DSLContext postgresqlDSLContext(@Qualifier("configPostgreSql") DataSourceProperties properties) {
        return DSL.using(
                properties.getUrl(), properties.getUsername(), properties.getPassword());
    }
}

package com.classicmodels;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication(exclude = { R2dbcAutoConfiguration.class })
@EnableWebSecurity
public class MainApplication {
 
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}

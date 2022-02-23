package com.classicmodels;

import com.classicmodels.service.ClassicModelsService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.context.annotation.Bean;

// This application produces an exception of type:
// com.mysql.cj.jdbc.exceptions.MySQLTransactionRollbackException: 
// Deadlock found when trying to get lock; try restarting transaction
    
// However, the database will retry until one of the transactions (e.g., A) succeeds

@SpringBootApplication(exclude = {R2dbcAutoConfiguration.class})
public class MainApplication {    
    
    private final ClassicModelsService classicModelsService;

    public MainApplication(ClassicModelsService classicModelsService) {
        this.classicModelsService = classicModelsService;
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    public ApplicationRunner init() {
        return args -> {

            classicModelsService.fetchProductsViaTwoTransactions();
        };
    }
}

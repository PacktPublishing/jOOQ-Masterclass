package com.classicmodels;

import com.classicmodels.service.ClassicModelsService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.classicmodels", "jooq.generated.tables.daos"})
//@EntityScan(basePackages = {"com.classicmodels.entity"})
///@ComponentScan(basePackages = {"com.classicmodels"})
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

            // uses jOOQ
            System.out.println("Fetch employees and least salary:");
            System.out.println(classicModelsService.fff());

            // uses Spring Data JPA
            System.out.println("Fetch the employees  by job title:");
           System.out.println(classicModelsService.fetchByJobTitle("Sales Rep"));
           
           classicModelsService.x();
        };
    }
}

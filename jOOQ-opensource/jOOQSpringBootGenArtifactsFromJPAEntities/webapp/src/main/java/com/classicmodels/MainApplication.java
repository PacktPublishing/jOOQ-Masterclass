package com.classicmodels;

import com.classicmodels.service.ClassicModelsService;
import java.util.Arrays;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.classicmodels"})
@EntityScan(basePackages = {"com.classicmodels.entity"})
@ComponentScan(basePackages = {"com.classicmodels"})
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
            /*
            System.out.println("Fetch employees and least salary:");
            System.out.println(hrService.fetchEmployeesAndLeastSalary());
            
            System.out.println("Fetch employees and least salary:");
            System.out.println(hrService.fetchEmployeesAndLeastSalary());
            
            System.out.println("Fetch the employees names as CSV");
            System.out.println(hrService.fetchEmployeesFirstNamesAsCsv());
             */
            System.out.println("Fetch the employees by title (1)");
            System.out.println(classicModelsService.fetchByJobTitleCntr("Sales Rep"));

            System.out.println("Fetch the employees by title (2)");
            System.out.println(classicModelsService.fetchByJobTitleJpql("Sales Rep"));
            
            System.out.println("Fetch the employees by title (3)");
            System.out.println(classicModelsService.fetchByJobTitleNative("Sales Rep"));
        };
    }
}

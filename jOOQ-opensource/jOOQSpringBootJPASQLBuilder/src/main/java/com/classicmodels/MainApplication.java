package com.classicmodels;

import com.classicmodels.service.HRService;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
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

    private final HRService hrService;

    public MainApplication(HRService hrService) {
        this.hrService = hrService;
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    public ApplicationRunner init() {
        return args -> {

            System.out.println("Fetch employees and least salary:");
            System.out.println(hrService.fetchEmployeesAndLeastSalary());

            System.out.println("Fetch employees and least salary via contructor mapping:");
            System.out.println(hrService.findEmployeesAndLeastSalaryCntr());

            System.out.println("Fetch employees in city:");
            System.out.println(hrService.fetchEmployeeInCity("Boston"));
            
            System.out.println("Fetch employees by job title:");
            System.out.println(hrService.fetchByJobTitle("Sales Manager (APAC)"));
        };
    }

    @Bean
    public Hibernate5Module hibernate5Module() {
        return new Hibernate5Module();
    }
}

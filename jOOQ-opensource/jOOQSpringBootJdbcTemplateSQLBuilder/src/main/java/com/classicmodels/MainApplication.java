package com.classicmodels;

import com.classicmodels.pojo.Manager;
import com.classicmodels.service.ClassicModelsService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MainApplication {

    private final ClassicModelsService orderPaymentService;

    public MainApplication(ClassicModelsService orderPaymentService) {
        this.orderPaymentService = orderPaymentService;
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    public ApplicationRunner init() {
        return args -> {

            System.out.println("Example: Fetched manager with id: 1");
            Manager manager = orderPaymentService.fetchManager(1L);
            System.out.println(manager);                                      
        };
    }
}
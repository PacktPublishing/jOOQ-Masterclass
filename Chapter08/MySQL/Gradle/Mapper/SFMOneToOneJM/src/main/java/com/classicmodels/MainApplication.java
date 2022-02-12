package com.classicmodels;

import com.classicmodels.pojo.SimpleCustomer;
import com.classicmodels.service.ClassicModelService;
import java.util.List;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = {R2dbcAutoConfiguration.class})
public class MainApplication {

    private final ClassicModelService classicModelService;

    public MainApplication(ClassicModelService classicModelService) {
        this.classicModelService = classicModelService;
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    public ApplicationRunner init() {
        return args -> {

            System.out.println("Sample: Fetch customers having a credit limit smaller than 50000:");
            List<SimpleCustomer> result = classicModelService.fetchCustomerByCreditLimit(50000.0f);
            System.out.println(result);
        };
    }
}

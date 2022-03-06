package com.classicmodels;

import com.classicmodels.service.ClassicModelsService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.context.annotation.Bean;

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

            classicModelsService.loadProductsAsc(20, 5);
            classicModelsService.loadProductsDesc(20, 5);
            classicModelsService.loadEmployeesOfficeCodeAscSalaryDesc("1", 75000, 10);
            classicModelsService.loadEmployeesOfficeCodeAscSalaryAsc("1", 75000, 10);
            classicModelsService.loadOrderdetailPageOrderIdAscProductIdQuantityOrderedDesc(10100, 23, 30, 10);
            classicModelsService.loadProductsBuyPriceGtMsrp(0, 5);
            classicModelsService.loadProductlineEmbeddedKey("Ships", 433823, 3);
            classicModelsService.loadOrderdetailPageGroupBy(10000, 5);
        };
    }
}

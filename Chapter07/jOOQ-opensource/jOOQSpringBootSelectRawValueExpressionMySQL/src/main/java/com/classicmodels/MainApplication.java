package com.classicmodels;

import com.classicmodels.service.ClassicModelsService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
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

            System.out.println("Sample: Call fetchProductsByVendorScaleAndProductLine(String vendor, String scale, String productLine):");
            System.out.println(classicModelsService
                    .fetchProductsByVendorScaleAndProductLine("Carousel DieCast Legends", "1:24", "Classic Cars"));
            
            System.out.println("Sample: Call fetchProductByIdVendorAsCarouselDieCastLegendsScaleAndProductLine(Long productId):");
            System.out.println(classicModelsService
                    .fetchProductByIdVendorAsCarouselDieCastLegendsScaleAndProductLine(63L));
            
            System.out.println("Sample: Call fetchProductsByVendorScaleAndProductLineIn():");
            System.out.println(classicModelsService
                    .fetchProductsByVendorScaleAndProductLineIn());
            
            System.out.println("Sample: Call fetchProductsByVendorScaleAndProductLineInCollection():");
            System.out.println(classicModelsService
                    .fetchProductsByVendorScaleAndProductLineInCollection());
            
            System.out.println("Sample: Call fetchProductsByVendorScaleAndProductLineInSelect():");
            System.out.println(classicModelsService
                    .fetchProductsByVendorScaleAndProductLineInSelect());
            
            System.out.println("Sample: Call fetchOrdersBetweenOrderDateAndShippedDate():");
            System.out.println(classicModelsService
                    .fetchOrdersBetweenOrderDateAndShippedDate());                        
        };
    }
}

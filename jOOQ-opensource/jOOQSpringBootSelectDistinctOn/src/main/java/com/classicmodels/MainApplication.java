package com.classicmodels;

import com.classicmodels.service.ClassicModelsService;
import java.util.List;
import jooq.generated.tables.pojos.Product;
import jooq.generated.tables.pojos.Sale;
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

            System.out.println("Sample: Call fetchProductsByVendorScale():");
            List<Product> products = classicModelsService.fetchProductsByVendorScale();
            System.out.println(products);
            
            System.out.println("Sample: Call fetchEmployeeNumberOfMaxSalePerFiscalYear():");
            List<Sale> employees = classicModelsService.fetchEmployeeNumberOfMaxSalePerFiscalYear();
            System.out.println(employees);                        
        };
    }
}

package com.classicmodels;

import com.classicmodels.model.ProductLine;
import com.classicmodels.service.ClassicModelsService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = {R2dbcAutoConfiguration.class})
public class MainApplication {

    private final ClassicModelsService productLineService;

    public MainApplication(ClassicModelsService productLineService) {
        this.productLineService = productLineService;
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    public ApplicationRunner init() {
        return args -> {
            System.out.println("\n\nExample: Fetch 'productline' and 'product'");
            Iterable<ProductLine> productlinesAndProduct = productLineService.fetchProductLineAndProduct();
            productlinesAndProduct.iterator().forEachRemaining(System.out::println);
            
            System.out.println("\n\nExample: Update a product line description via Spring Data JDBC");
            productLineService.updateProductLineDescription("Classic Cars");
            
            System.out.println("\n\nExample: Update a product line description via jOOQ");
            productLineService.updateProductLineDescriptionJooq("Classic Cars");
        };
    }
}

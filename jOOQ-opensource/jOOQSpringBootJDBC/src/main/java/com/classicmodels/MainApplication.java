package com.classicmodels;

import com.classicmodels.model.ProductLine;
import com.classicmodels.service.ProductLineService;
import java.util.List;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MainApplication {

    private final ProductLineService productLineService;

    public MainApplication(ProductLineService productLineService) {
        this.productLineService = productLineService;
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    public ApplicationRunner init() {
        return args -> {
            System.out.println("\n\nSample: Fetch 'productline' and 'product'");
            Iterable<ProductLine> productlinesAndProduct = productLineService.fetchProductLineAndProduct();
            productlinesAndProduct.iterator().forEachRemaining(System.out::println);
            
            System.out.println("\n\nSample: Fetch only 'productline'");
            List<ProductLine> productlines = productLineService.fetchOnlyProductLine();
            productlines.forEach(System.out::println);
        };
    }
}

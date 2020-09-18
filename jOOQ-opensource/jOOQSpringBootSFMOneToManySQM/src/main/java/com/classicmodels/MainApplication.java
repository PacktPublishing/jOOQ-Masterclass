package com.classicmodels;

import com.classicmodels.pojo.ProductLineDTO;
import com.classicmodels.service.ProductService;
import java.util.List;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MainApplication {

    private final ProductService productRepository;

    public MainApplication(ProductService productRepository) {
        this.productRepository = productRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    public ApplicationRunner init() {
        return args -> {

            System.out.println("Sample: Fetch product lines with products:");
            List<ProductLineDTO> products = productRepository.fetchProductLineWithProducts();
            System.out.println(products);
        };
    }
}

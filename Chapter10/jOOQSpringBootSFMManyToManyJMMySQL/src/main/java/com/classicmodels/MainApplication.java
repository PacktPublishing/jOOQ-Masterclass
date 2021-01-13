package com.classicmodels;

import com.classicmodels.pojo.SimpleManager;
import com.classicmodels.service.ClasicModelsService;
import java.util.List;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MainApplication {

    private final ClasicModelsService clasicModelsService;

    public MainApplication(ClasicModelsService clasicModelsService) {
        this.clasicModelsService = clasicModelsService;
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    public ApplicationRunner init() {
        return args -> {

            System.out.println("Sample: Fetch managers and offices:");
            List<SimpleManager> result = clasicModelsService.fetchManagerAndOffice();
            System.out.println(result);                       
        };
    }
}

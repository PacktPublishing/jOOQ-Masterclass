package com.classicmodels;

import com.classicmodels.pojo.Office;
import com.classicmodels.service.ClassicModelsService;
import java.util.List;
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

            System.out.println("Fetching offices from 'NA' territory (1):");
            List<Office> offices1 = classicModelsService.fetchOfficesInTerritory1("NA");
            System.out.println(offices1);
            
            System.out.println("Fetching offices from 'NA' territory (2):");
            List<Office> offices2 = classicModelsService.fetchOfficesInTerritory2("NA");
            System.out.println(offices2);
            
            System.out.println("Fetching offices from 'NA' territory (3):");
            List<Office> offices3 = classicModelsService.fetchOfficesInTerritory3("NA");
            System.out.println(offices3);
            
            System.out.println("Fetching offices from 'NA' territory (4):");
            List<Office> offices4 = classicModelsService.fetchOfficesInTerritory4("NA");
            System.out.println(offices4);
            
            System.out.println("Fetching offices from 'NA' territory (5):");
            String offices5 = classicModelsService.fetchOfficesInTerritory5("NA");
            System.out.println(offices5);
            
            System.out.println("Fetching offices from 'NA' territory (6):");
            List<Office> offices6 = classicModelsService.fetchOfficesInTerritory6("NA");
            System.out.println(offices6);
        };
    }
}
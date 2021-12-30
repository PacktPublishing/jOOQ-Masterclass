package com.classicmodels;

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

            System.out.println("Delete query:");
            int affectedRows = classicModelsService.jooqQuery();
            System.out.println("Affected rows: " + affectedRows);

            System.out.println("Select query:");
            List<String> result = classicModelsService.jooqResultQuery();
            System.out.println(result);
            
            System.out.println("Iterable query results:");
            classicModelsService.iterableResultQuery();
        };
    }
}

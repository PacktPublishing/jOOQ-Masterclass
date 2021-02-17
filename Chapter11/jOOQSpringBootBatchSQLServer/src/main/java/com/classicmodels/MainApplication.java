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

            classicModelsService.batchInserts();
            classicModelsService.batchUpdates();
            classicModelsService.batchDeletes();
            classicModelsService.batchMerges();
            classicModelsService.batchStores();
            classicModelsService.combineBatch();
            classicModelsService.batchCollectionOfObjects();
            classicModelsService.batchedInsertsAndUpdates1();
            classicModelsService.batchedInsertsAndUpdates2();
            classicModelsService.batchedAndReturn();
            classicModelsService.batchedConnectionUsage();
            classicModelsService.batchedRecords();
            classicModelsService.batchingOneToMany();
        };
    }
}

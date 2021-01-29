package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import java.util.concurrent.CompletableFuture;
import org.springframework.stereotype.Service;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    public void insertUpdateDeleteAsync() {

        System.out.println("Start .................................");

        // Wait until one is done
        CompletableFuture<Void> cf = CompletableFuture.allOf(
                classicModelsRepository.insertAsync(),
                classicModelsRepository.updateAsync(),
                classicModelsRepository.deleteAsync()
        );
                
        cf.join();

        System.out.println("Done ................................." + cf);
    }
}

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

    public String fetchCompanyAsync() {

        CompletableFuture<String>[] fetchedCf = new CompletableFuture[]{
            classicModelsRepository.fetchManagersAsync(),
            classicModelsRepository.fetchOfficesAsync(),
            classicModelsRepository.fetchEmployeesAsync()};

        // Wait until they are all done
        CompletableFuture<Void> allFetchedCf = CompletableFuture.allOf(fetchedCf);
        allFetchedCf.join();

        // collect the final result
        return allFetchedCf.thenApply(r -> {
            StringBuilder result = new StringBuilder();

            for (CompletableFuture<String> cf : fetchedCf) {
                result.append(cf.join());
            }

            return result.toString();
        }).join();
    }
}

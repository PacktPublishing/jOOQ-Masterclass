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
    
    public Integer updateDbAsync() {

        CompletableFuture<Integer>[] fetchedCf = new CompletableFuture[]{
            classicModelsRepository.updateCreditLimitAsync(),
            classicModelsRepository.deleteFailedTransactionsAsync()};

        // Wait until they are all done
        CompletableFuture<Void> allFetchedCf = CompletableFuture.allOf(fetchedCf);
        allFetchedCf.join();

        // collect the final result
        return allFetchedCf.thenApply(r -> {
            Integer affectedRows = 0;

            for (CompletableFuture<Integer> cf : fetchedCf) {
                affectedRows += cf.join();
            }

            return affectedRows;
        }).join();
    }
}

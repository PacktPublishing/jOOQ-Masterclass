package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.stereotype.Service;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    public List<Integer> executeJOOQTransactions() {

        CompletableFuture<Integer>[] fetchedCf = new CompletableFuture[]{
            classicModelsRepository.executeFirstJOOQTransaction(),
            classicModelsRepository.executeSecondJOOQTransaction()};

        // Wait until they are all done
        CompletableFuture<Void> allFetchedCf = CompletableFuture.allOf(fetchedCf);
        allFetchedCf.join();

        // collect the final result
        return allFetchedCf.thenApply(r -> {
            List<Integer> result = new ArrayList<>();

            for (CompletableFuture<Integer> cf : fetchedCf) {
                result.add(cf.join());
            }

            return result;
        }).join();
    }
}

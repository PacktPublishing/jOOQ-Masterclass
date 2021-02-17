package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import org.springframework.stereotype.Service;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    public void batchInserts() {

        classicModelsRepository.batchInserts();
    }

    public void batchUpdates() {

        classicModelsRepository.batchUpdates();
    }

    public void batchDeletes() {

        classicModelsRepository.batchDeletes();
    }

    public void batchMerges() {

        classicModelsRepository.batchMerges();
    }

    public void batchStores() {

        classicModelsRepository.batchStores();
    }

    public void combineBatch() {

        classicModelsRepository.combineBatch();
    }

    public void batchCollectionOfObjects() {
        
        classicModelsRepository.batchCollectionOfObjects();
    }
    
    public void batchedInsertsAndUpdates1() {

        classicModelsRepository.batchedInsertsAndUpdates1();
    }

    public void batchedInsertsAndUpdates2() {

        classicModelsRepository.batchedInsertsAndUpdates2();
    }
    
    public void batchedAndReturn() {
        
        classicModelsRepository.batchedAndReturn();
    }
    
    public void batchedRecords() {
        
        classicModelsRepository.batchedRecords();
    }

    public void batchedConnectionUsage() {

        classicModelsRepository.batchedConnectionUsage();
    }
    
    public void batchingOneToMany() {
        
        classicModelsRepository.batchingOneToMany();
    }
}

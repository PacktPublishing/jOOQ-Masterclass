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

    public void batchMerges1() {

        classicModelsRepository.batchMerges1();
    }
    
    public void batchMerges2() {

        classicModelsRepository.batchMerges2();
    }
    
    public void batchMerges3() {

        classicModelsRepository.batchMerges3();
    }

    public void batchStoresSimple() {

        classicModelsRepository.batchStoresSimple();
    }
    
    public void batchStoresPreparedStatement1() {
        
        classicModelsRepository.batchStoresPreparedStatement1();
    }
    
    public void batchStoresPreparedStatement2() {
        
        classicModelsRepository.batchStoresPreparedStatement2();
    }
    
    public void batchStoresPreparedStatement3() {
        
        classicModelsRepository.batchStoresPreparedStatement3();
    }
    
    public void batchStoresPreparedStatement4() {
        
        classicModelsRepository.batchStoresPreparedStatement4();
    }
    
    public void batchStoresStaticStatement() {
        
        classicModelsRepository.batchStoresStaticStatement();
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

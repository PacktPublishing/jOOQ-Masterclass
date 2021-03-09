package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import org.springframework.stereotype.Service;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    public void fetchWithNoTransaction() {

        classicModelsRepository.fetchWithNoTransaction();
    }
    
    public void fetchReadOnlyTransaction() {
        
        classicModelsRepository.fetchReadOnlyTransaction();
    }
    
    public void fetchReadOnlyTransactionTemplate() {
        
        classicModelsRepository.fetchReadOnlyTransactionTemplate();
    }
    
    public void fetchJOOQTransaction() {
        
        classicModelsRepository.fetchJOOQTransaction();
    }      
    
    public void updateNoTransaction() {
        
        classicModelsRepository.updateNoTransaction();
    }
        
    public void updateWithTransaction() {
        
        classicModelsRepository.updateWithTransaction();
    }
    
    public void updateWithTransactionTemplate() {
        
        classicModelsRepository.updateWithTransactionTemplate();
    }
            
    public void updateWithJOOQTransaction() {
        
        classicModelsRepository.updateWithJOOQTransaction();
    }    
    
    public void fetchAndStreamWithTransactional() {
        
        classicModelsRepository.fetchAndStreamWithTransactional();
    }
    
    public void fetchAndStreamWithJOOQTransaction() {
        
        classicModelsRepository.fetchAndStreamWithJOOQTransaction();
    }
    
    public void fetchAndStreamWithTransactionTemplate() {
        
        classicModelsRepository.fetchAndStreamWithTransactionTemplate();
    }        
}

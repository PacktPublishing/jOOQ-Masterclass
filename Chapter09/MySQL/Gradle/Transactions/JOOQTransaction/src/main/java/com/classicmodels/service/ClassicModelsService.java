package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import org.springframework.stereotype.Service;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }
    
    public void rollbackJOOQTransaction() {
        
        classicModelsRepository.rollbackJOOQTransaction();
    }

    public void dontRollbackJOOQTransaction() {

        classicModelsRepository.dontRollbackJOOQTransaction();
    }
    
    
    public void nestedJOOQTransaction() {
        
        classicModelsRepository.nestedJOOQTransaction();
    }
    
    public void nestedDontRollbackOuterJOOQTransaction() {
        
        classicModelsRepository.nestedDontRollbackOuterJOOQTransaction();
    }
}

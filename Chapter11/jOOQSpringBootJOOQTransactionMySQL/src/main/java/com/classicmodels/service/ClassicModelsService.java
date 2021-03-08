package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import org.springframework.stereotype.Service;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    public void fetchNoTransaction() {

        classicModelsRepository.fetchNoTransaction();
    }
    
    public void fetchReadOnlyTransaction() {
        
        classicModelsRepository.fetchReadOnlyTransaction();
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
    
    public void updateWithJOOQTransaction() {
        
        classicModelsRepository.updateWithJOOQTransaction();
    }    
}

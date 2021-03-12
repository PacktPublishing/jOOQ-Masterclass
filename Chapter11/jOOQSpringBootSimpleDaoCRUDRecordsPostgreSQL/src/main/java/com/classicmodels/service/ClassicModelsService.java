package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import org.springframework.stereotype.Service;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }
    
    public void insertProductViajOOQDao() {
        
        classicModelsRepository.insertProductViajOOQDao();
    }
    
    public void updateProductViajOOQDao() {
        
        classicModelsRepository.updateProductViajOOQDao();
    }
    
    public void mergeProductViajOOQDao() {
        
        classicModelsRepository.mergeProductViajOOQDao();
    }
    
    public void deleteProductViajOOQDao() {
        
        classicModelsRepository.deleteProductViajOOQDao();
    }
    
    public void recordToPojo() {
        
        classicModelsRepository.recordToPojo();
    }
}
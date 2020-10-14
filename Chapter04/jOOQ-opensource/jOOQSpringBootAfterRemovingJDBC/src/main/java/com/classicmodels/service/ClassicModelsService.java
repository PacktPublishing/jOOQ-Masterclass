package com.classicmodels.service;

import com.classicmodels.model.ProductLine;
import com.classicmodels.repository.ClassicModelsRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;        
    }

    @Transactional(readOnly = true)
    public List<ProductLine> fetchProductLineAndProduct() {
        
        return classicModelsRepository.findProductLineAndProduct();
    }

    @Transactional(readOnly = true)
    public List<ProductLine> fetchProductLine() {
        
        return classicModelsRepository.findProductLine();
    }    
        
    @Transactional
    public void updateProductLineDescription(String id) {
                
        classicModelsRepository.updateProductLineDescription(id);
    }
}
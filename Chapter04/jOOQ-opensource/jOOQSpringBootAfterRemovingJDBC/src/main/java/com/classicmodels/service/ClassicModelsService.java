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
    public List<ProductLine> fetchProductLineAndProductJooq() {
        
        return classicModelsRepository.findProductLineAndProductJooq();
    }

    @Transactional(readOnly = true)
    public List<ProductLine> fetchProductLineJooq() {
        
        return classicModelsRepository.findProductLineJooq();
    }    
        
    @Transactional
    public void updateProductLineDescriptionJooq() {
                
        classicModelsRepository.updateProductLineDescriptionJooq();
    }
}
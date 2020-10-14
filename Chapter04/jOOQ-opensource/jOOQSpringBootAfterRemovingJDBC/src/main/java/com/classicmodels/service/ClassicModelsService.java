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
        // Spring Data JDBC always fetches the entire aggregate via N+1 queries
        return classicModelsRepository.findProductLineAndProduct();
    }

    @Transactional(readOnly = true)
    public List<ProductLine> fetchProductLineJooq() {
        // jOOQ fetches only the data from 'productline'
        return classicModelsRepository.findProductLineJooq();
    }    
        
    @Transactional
    public void updateProductLineDescriptionJooq() {
        
        // jOOQ uses a single update query to achieve the same result
        classicModelsRepository.updateProductLineDescriptionJooq();
    }
}
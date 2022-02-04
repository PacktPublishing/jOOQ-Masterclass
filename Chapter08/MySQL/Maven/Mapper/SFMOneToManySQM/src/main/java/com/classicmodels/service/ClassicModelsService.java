package com.classicmodels.service;

import com.classicmodels.pojo.SimpleProductLine;
import com.classicmodels.repository.ClassicModelsRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ClassicModelsService {

    private final ClassicModelsRepository productRepository;

    public ClassicModelsService(ClassicModelsRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    public List<SimpleProductLine> fetchProductLineWithProducts() {

        return productRepository.findProductLineWithProducts();
    }

}
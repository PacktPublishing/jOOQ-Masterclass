package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import org.springframework.stereotype.Service;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    public void callAll() {

        classicModelsRepository.insertOneSale();                    // EXAMPLE 1
        classicModelsRepository.insertTwoSale();                    // EXAMPLE 2        
        classicModelsRepository.insertRecordSale();                 // EXAMPLE 3        
    }
}

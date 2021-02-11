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
        classicModelsRepository.selectCommonlyUsedValues1();     // EXAMPLE 1
        classicModelsRepository.selectCommonlyUsedValues2();     // EXAMPLE 2
        classicModelsRepository.selectCommonlyUsedValues3();     // EXAMPLE 3
        classicModelsRepository.deleteSales();                   // EXAMPLE 4
    }
}

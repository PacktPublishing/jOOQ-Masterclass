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

            classicModelsRepository.insertAllDefaultsInManager();                       // EXAMPLE 1
            classicModelsRepository.insertSomeDefaultsValInProduct();                   // EXAMPLE 2
            classicModelsRepository.insertSomeDefaultsByTypeSomeExplicitInProduct();    // EXAMPLE 3
            classicModelsRepository.insertSomeImplicitDefaultsInProduct();              // EXAMPLE 4
            classicModelsRepository.insertDefaultsViaNewRecord();                       // EXAMPLE 5                    
    }
}
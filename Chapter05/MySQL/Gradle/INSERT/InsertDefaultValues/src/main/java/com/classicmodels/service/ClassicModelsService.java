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
            classicModelsRepository.insertSomeDefaultsInProduct();                      // EXAMPLE 2
            classicModelsRepository.insertSomeDefaultsValInProduct();                   // EXAMPLE 3
            classicModelsRepository.insertSomeDefaultsByTypeSomeExplicitInProduct();    // EXAMPLE 4
            classicModelsRepository.insertSomeImplicitDefaultsInProduct();              // EXAMPLE 5
            classicModelsRepository.insertDefaultsViaNewRecord();                       // EXAMPLE 6 
    }
}
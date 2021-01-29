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
        
        classicModelsRepository.fetchCustomerLazyOneByOne();            // EXAMPLE 1
        classicModelsRepository.fetchCustomerLazyFiveByFive();          // EXAMPLE 2
        classicModelsRepository.fetchCustomerLazyAndUpdate();           // EXAMPLE 3
        classicModelsRepository.fetchCustomerLazyAndRecordMapper();     // EXAMPLE 4
        classicModelsRepository.fetchExactlyOneRow();                   // EXAMPLE 5
        classicModelsRepository.fetchExactlyOneRowGlobal();             // EXAMPLE 6        
    }
}
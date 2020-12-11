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

        classicModelsRepository.joinEmployeeCustomerViaLeftJoin();           // EXAMPLE 1
        classicModelsRepository.joinEmployeeCustomerViaExists();             // EXAMPLE 2
        classicModelsRepository.joinEmployeeCustomerViaIn();                 // EXAMPLE 3
        classicModelsRepository.joinEmployeeCustomerViaLeftSemiJoin();       // EXAMPLE 4
        classicModelsRepository.joinEmployeeCustomerSaleViaLeftSemiJoin();   // EXAMPLE 5
        classicModelsRepository.joinEmployeeCustomerViaNotExists();          // EXAMPLE 6
        classicModelsRepository.joinEmployeeCustomerViaNotIn();              // EXAMPLE 7
        classicModelsRepository.joinEmployeeCustomerViaAntiJoin();           // EXAMPLE 8        
        classicModelsRepository.joinEmployeeCustomerSaleViaAntiJoin();       // EXAMPLE 9  
                
    }
}
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
        
        classicModelsRepository.joinEmployeeCustomerViaExists();               // EXAMPLE 1
        classicModelsRepository.joinEmployeeCustomerViaIn();                   // EXAMPLE 2
        classicModelsRepository.badEmployeeCustomerViaLeftJoinAndIsNotNull();  // EXAMPLE 3
        classicModelsRepository.joinEmployeeCustomerViaLeftSemiJoin();         // EXAMPLE 4
        classicModelsRepository.joinEmployeeCustomerSaleViaLeftSemiJoin();     // EXAMPLE 5
        classicModelsRepository.joinEmployeeCustomerViaNotExists();            // EXAMPLE 6
        classicModelsRepository.joinEmployeeCustomerViaNotIn();                // EXAMPLE 7
        classicModelsRepository.badEmployeeCustomerViaLeftJoinAndIsNull();     // EXAMPLE 8
        classicModelsRepository.joinEmployeeCustomerViaAntiJoin();             // EXAMPLE 9        
        classicModelsRepository.joinEmployeeCustomerSaleViaAntiJoin();         // EXAMPLE 10                  
    }
}
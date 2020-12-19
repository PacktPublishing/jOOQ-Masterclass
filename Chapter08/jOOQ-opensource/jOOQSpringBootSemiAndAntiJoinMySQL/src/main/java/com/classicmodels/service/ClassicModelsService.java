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
        classicModelsRepository.joinEmployeeCustomerPaymentViaLeftSemiJoin();  // EXAMPLE 6
        classicModelsRepository.joinEmployeeCustomerViaNotExists();            // EXAMPLE 7
        classicModelsRepository.joinEmployeeCustomerViaNotIn();                // EXAMPLE 8
        classicModelsRepository.badEmployeeCustomerViaLeftJoinAndIsNull();     // EXAMPLE 9
        classicModelsRepository.joinEmployeeCustomerViaAntiJoin();             // EXAMPLE 10        
        classicModelsRepository.joinEmployeeCustomerSaleViaAntiJoin();         // EXAMPLE 11                                 
    }
}
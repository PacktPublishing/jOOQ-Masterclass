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

        classicModelsRepository.simpleDeletes();                                                  // EXAMPLE 1
        classicModelsRepository.deletePayment();                                                  // EXAMPLE 2
        classicModelsRepository.deleteCustomerDetailViaNotIn();                                   // EXAMPLE 3
        classicModelsRepository.deleteOrderByAndLimit();                                          // EXAMPLE 4
        classicModelsRepository.deleteCascade();                                                  // EXAMPLE 5
        classicModelsRepository.deleteRecordImplicitWhere();                                      // EXAMPLE 6
        classicModelsRepository.moreDeleteRecordExamples();                                       // EXAMPLE 7
        classicModelsRepository.throwExceptionForDeleteWithoutWhereClause();                      // EXAMPLE 8                
        classicModelsRepository.deleteSaleReturning();                                            // EXAMPLE 9
        classicModelsRepository.deletePaymentReturning();                                         // EXAMPLE 10
        classicModelsRepository.deleteCascadeReturningProductLineMotorcyclesAndTrucksAndBuses();  // EXAMPLE 11        
    }
}

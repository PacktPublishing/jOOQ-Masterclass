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

        classicModelsRepository.implicitJoinOfficeEmployeeViaWhere();                   // EXAMPLE 1
        classicModelsRepository.implicitJoinOfficeEmployeeViaNavigationMethod();        // EXAMPLE 2
        classicModelsRepository.implicitJoinPaymentCustomerViaNavigationMethod();       // EXAMPLE 3
        classicModelsRepository.implicitJoinOrderCustomerEmployeeViaNavigationMethod(); // EXAMPLE 4        
        classicModelsRepository.selfJoinEmployee();                                     // EXAMPLE 5
        classicModelsRepository.selfJoinEmployeeViaNavigationMethod();                  // EXAMPLE 6
        classicModelsRepository.selfJoinComparingEmployeeViaNavigationMethod();         // EXAMPLE 7
        classicModelsRepository.selfJoinThreeTimes();                                   // EXAMPLE 8 
    }
}
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
        classicModelsRepository.findlEmployeeInOfficeStartingS();            // EXAMPLE 1
        classicModelsRepository.findEmployeeInOfficeNotMA();                 // EXAMPLE 2
        classicModelsRepository.findSaleLtAvg();                             // EXAMPLE 3
        classicModelsRepository.findEmployeeAndSale();                       // EXAMPLE 4
        classicModelsRepository.findSale();                                  // EXAMPLE 5
        classicModelsRepository.findSaleLtAvgAvg();                          // EXAMPLE 6
        classicModelsRepository.findPaymentForCustomerSignalGiftStores();    // EXAMPLE 7
        classicModelsRepository.insertIntoOrder();                           // EXAMPLE 8
        classicModelsRepository.insertAnotherTableInManager();               // EXAMPLE 9
        classicModelsRepository.updateEmployeeSalaryByJobTitle();            // EXAMPLE 10
        classicModelsRepository.deletePaymentWithCachingDateNotNull();       // EXAMPLE 11                
    }
}


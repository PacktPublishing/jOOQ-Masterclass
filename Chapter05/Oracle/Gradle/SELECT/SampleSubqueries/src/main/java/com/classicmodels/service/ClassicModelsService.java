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
        classicModelsRepository.findlEmployeeInOfficeStartingS();                      // EXAMPLE 1
        classicModelsRepository.findEmployeeInOfficeNotMA();                           // EXAMPLE 2
        classicModelsRepository.findSaleLtAvg();                                       // EXAMPLE 3
        classicModelsRepository.findEmployeesWithSalaryGeAvgPerOffice();               // EXAMPLE 4
        classicModelsRepository.findEmployeeAndSale();                                 // EXAMPLE 5        
        classicModelsRepository.orderSales();                                          // EXAMPLE 6
        classicModelsRepository.employeesAndNumberOfSales();                           // EXAMPLE 7
        classicModelsRepository.findSaleLtAvgAvg();                                    // EXAMPLE 8
        classicModelsRepository.findPaymentForCustomerSignalGiftStores();              // EXAMPLE 9
        classicModelsRepository.insertIntoOrder();                                     // EXAMPLE 10
        classicModelsRepository.insertAnotherTableInDailyActivity();                   // EXAMPLE 11
        classicModelsRepository.updateEmployeeSalaryByJobTitle();                      // EXAMPLE 12
        classicModelsRepository.deleteDepartmentWithAccuredLiabilitiesNotNull();       // EXAMPLE 13                                       
    }
}
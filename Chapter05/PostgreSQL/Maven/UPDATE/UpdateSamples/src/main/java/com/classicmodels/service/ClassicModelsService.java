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
        classicModelsRepository.updateOffice();                                                    // EXAMPLE 1
        classicModelsRepository.updateRowOffice();                                                 // EXAMPLE 2              
        classicModelsRepository.updateCustomerCreditLimitAsMaxPaymentInvoice();                    // EXAMPLE 3
        classicModelsRepository.updateEmployeeSalaryBySaleCount();                                 // EXAMPLE 4
        classicModelsRepository.updateNewRecordOffice();                                           // EXAMPLE 5
        classicModelsRepository.updateOfficeAddressAsPresidentName();                              // EXAMPLE 6
        classicModelsRepository.updateProductBuyPriceWithFirstPriceEach();                         // EXAMPLE 7
        classicModelsRepository.updateOfficeReturning();                                           // EXAMPLE 8
        classicModelsRepository.updateEmployeeSalaryAsAvgSaleAndCustomersCreditAsDoubleSalary();   // EXAMPLE 9
        classicModelsRepository.updateSaleHot();                                                   // EXAMPLE 10
        classicModelsRepository.throwExceptionForUpdateWithoutWhereClause();                       // EXAMPLE 11
    }
}

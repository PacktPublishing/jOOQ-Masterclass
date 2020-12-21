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
        classicModelsRepository.findEmployeesBySumSales();                             // EXAMPLE 1
        classicModelsRepository.findCustomerFullNameCityCountry();                     // EXAMPLE 2
        classicModelsRepository.findOfficeAndNoOfEmployee();                           // EXAMPLE 3
        classicModelsRepository.findMaxSalePerFiscalYearAndEmployee();                 // EXAMPLE 4
        classicModelsRepository.findEmployeeWithAvgSaleLtSumSales();                   // EXAMPLE 5
        classicModelsRepository.findOfficeAndEmployeeMaxAndAvgSalary();                // EXAMPLE 6
        classicModelsRepository.findCustomerWithMoreThan10Sales();                     // EXAMPLE 7
        classicModelsRepository.findProductQuantityOrderedGt70();                      // EXAMPLE 8 
        classicModelsRepository.findProductWithMsrpGtSellPrice();                      // EXAMPLE 9 
        classicModelsRepository.findProductWithAvgBuyPriceGtAnyPriceEach();            // EXAMPLE 10
        classicModelsRepository.findProductWithAvgBuyPriceGtAllPriceEach();            // EXAMPLE 11
        classicModelsRepository.findUnprocessedPayments();                             // EXAMPLE 12
        classicModelsRepository.findEmployeeNumberWithMoreSalesIn2005Than2003();       // EXAMPLE 13
        classicModelsRepository.updateCustomerCreditLimit();                           // EXAMPLE 14
        classicModelsRepository.deletePaymentOfCustomerCreditLimitGt150000();          // EXAMPLE 15
        classicModelsRepository.insertPaymentInBankTransaction();                      // EXAMPLE 16                
    }
}
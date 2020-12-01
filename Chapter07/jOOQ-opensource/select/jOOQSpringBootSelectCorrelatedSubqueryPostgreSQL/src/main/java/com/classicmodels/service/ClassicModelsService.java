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
        classicModelsRepository.findProductMaxBuyPriceByProductionLine();              // EXAMPLE 1
        classicModelsRepository.findEmployeesBySumSales();                             // EXAMPLE 2
        classicModelsRepository.findCustomerFullNameCityCountry();                     // EXAMPLE 3
        classicModelsRepository.findOfficeAndNoOfEmployee();                           // EXAMPLE 4
        classicModelsRepository.findMaxSalePerFiscalYearAndEmployee();                 // EXAMPLE 5
        classicModelsRepository.findEmployeeWithAvgSaleLtSumSales();                   // EXAMPLE 6
        classicModelsRepository.findOfficeAndEmployeeMaxAndAvgSalary();                // EXAMPLE 7
        classicModelsRepository.findCustomerWithMoreThan10Sales();                     // EXAMPLE 8
        classicModelsRepository.findOrderdetailWithQuantityInStockGtQuantityOrdered(); // EXAMPLE 9
        classicModelsRepository.findProductQuantityOrderedGt70();                      // EXAMPLE 10 
        classicModelsRepository.findProductWithMsrpGtSellPrice();                      // EXAMPLE 11 
        classicModelsRepository.findProductWithAvgBuyPriceGtAnyPriceEach();            // EXAMPLE 12
        classicModelsRepository.findProductWithAvgBuyPriceGtAllPriceEach();            // EXAMPLE 13
        classicModelsRepository.findUnprocessedPayments();                             // EXAMPLE 14
        classicModelsRepository.findEmployeeNumberWithMoreSalesIn2005Than2003();       // EXAMPLE 15
        classicModelsRepository.updateCustomerCreditLimit();                           // EXAMPLE 16
        classicModelsRepository.deletePaymentOfCustomerCreditLimitGt150000();          // EXAMPLE 17
        classicModelsRepository.insertPaymentInOrder();                                // EXAMPLE 18                
    }
}
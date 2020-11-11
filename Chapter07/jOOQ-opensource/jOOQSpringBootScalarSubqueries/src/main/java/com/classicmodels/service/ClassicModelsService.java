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
        classicModelsRepository.findSalaryGeAvgPlus25000();
        classicModelsRepository.findMinAndRoundMinInvoiceAmount();
        classicModelsRepository.findEmployeeAndNoOfSale();
        classicModelsRepository.findOfficeAndNoOfEmployee();
        classicModelsRepository.findBaseSalary();
        classicModelsRepository.findOfficeAndEmployeeMaxAndAvgSalary();
        classicModelsRepository.findEmployeeWithAvgSaleLtSumSales();
        classicModelsRepository.findEmployeeWithSalaryGt();
        classicModelsRepository.findMaxSalePerFiscalYearAndEmployee();
        classicModelsRepository.insertEmployee();
        classicModelsRepository.deletePaymentsOfAtelierGraphique();
        classicModelsRepository.updateCustomerCreditLimit();
    }       
}
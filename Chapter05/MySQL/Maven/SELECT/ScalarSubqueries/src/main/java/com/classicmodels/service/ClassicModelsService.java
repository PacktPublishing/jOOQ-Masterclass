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
        classicModelsRepository.findSalaryGeAvgPlus25000();                 // EXAMPLE 1
        classicModelsRepository.findMinAndRoundMinInvoiceAmount();          // EXAMPLE 2
        classicModelsRepository.findBaseSalary();                           // EXAMPLE 3
        classicModelsRepository.findEmployeeWithSalaryGt();                 // EXAMPLE 4
        classicModelsRepository.insertProduct();                            // EXAMPLE 5
        classicModelsRepository.deleteBankTransactionsOfAtelierGraphique(); // EXAMPLE 6
        classicModelsRepository.updateEmployeeSalary();                     // EXAMPLE 7
    }       
}
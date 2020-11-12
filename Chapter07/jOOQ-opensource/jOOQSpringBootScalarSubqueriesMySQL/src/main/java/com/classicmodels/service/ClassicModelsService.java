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
        classicModelsRepository.findBaseSalary();
        classicModelsRepository.findEmployeeWithSalaryGt();
        classicModelsRepository.insertEmployee();
        classicModelsRepository.deletePaymentsOfAtelierGraphique();
        classicModelsRepository.updateEmployeeSalary();
    }       
}
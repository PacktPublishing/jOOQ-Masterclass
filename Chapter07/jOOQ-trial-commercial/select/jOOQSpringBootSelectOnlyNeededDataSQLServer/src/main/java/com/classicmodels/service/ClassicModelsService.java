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
        classicModelsRepository.findOrderAllFields();             // EXAMPLE 1
        classicModelsRepository.findOrderExplicitFields();        // EXAMPLE 2
        classicModelsRepository.findOrderAsteriskExcept();        // EXAMPLE 3
        classicModelsRepository.findOrderAndSale();               // EXAMPLE 4
        classicModelsRepository.findOrderExceptAndSale();         // EXAMPLE 5
        classicModelsRepository.findOfficeUseAliasForCity();      // EXAMPLE 6
        classicModelsRepository.findOfficeUseNvlForCity();        // EXAMPLE 7
        classicModelsRepository.findSaleGt5000();                 // EXAMPLE 8
        classicModelsRepository.findSaleMul025();                 // EXAMPLE 9
        classicModelsRepository.findOfficeInCityByCertainSort();  // EXAMPLE 10
        classicModelsRepository.findEmployeeSalary60000(true);    // EXAMPLE 11
        classicModelsRepository.findEmployeeLimit();              // EXAMPLE 12
        classicModelsRepository.findEmployeeLimitOffset();        // EXAMPLE 13
        classicModelsRepository.findEmployeeLimitAndOffset();     // EXAMPLE 14 
        classicModelsRepository.limit1InJoinedTable();            // EXAMPLE 15
        classicModelsRepository.decomposeSelect();                // EXAMPLE 16
        classicModelsRepository.decomposeSelectAndFrom();         // EXAMPLE 17        
    }
}

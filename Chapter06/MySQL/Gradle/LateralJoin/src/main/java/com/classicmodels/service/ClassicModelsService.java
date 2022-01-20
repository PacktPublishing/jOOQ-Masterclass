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

        classicModelsRepository.lateralOfficeHasDepartments();                     // EXAMPLE 1
        classicModelsRepository.leftOuterJoinLateralOfficeHasDepartments();        // EXAMPLE 2 
        classicModelsRepository.lateralEmployeeAvgSales();                         // EXAMPLE 3
        classicModelsRepository.lateralOfficeCityCountryHasDepartments();          // EXAMPLE 4         
        classicModelsRepository.lateralDepartmentUnnest();                         // EXAMPLE 5
        classicModelsRepository.findTop3SalesPerEmployee();                        // EXAMPLE 6
        classicModelsRepository.findTop3OrderedProductsIn2003();                   // EXAMPLE 7        
    }
}

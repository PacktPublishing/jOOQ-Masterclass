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

        classicModelsRepository.lateralOfficeHasDepartments();                                   // EXAMPLE 1
        classicModelsRepository.leftOuterJoinLateralOfficeHasDepartments();                      // EXAMPLE 2 
        classicModelsRepository.lateralEmployeeAvgSales();                                       // EXAMPLE 3
        classicModelsRepository.lateralOfficeCityCountryHasDepartments();                        // EXAMPLE 4         
        classicModelsRepository.lateralDepartmentUnnest();                                       // EXAMPLE 5                
        classicModelsRepository.lateralDepartmentUnnestOrdinality();                             // EXAMPLE 6
        classicModelsRepository.findTop3SalesPerEmployee();                                      // EXAMPLE 7
        classicModelsRepository.findTop3OrderedProductsIn2003();                                 // EXAMPLE 8
        classicModelsRepository.findTop3SalesPerEmployeeViaTableValuedFunction();                // EXAMPLE 9
        classicModelsRepository.crossApplyOfficeHasDepartments();                                // EXAMPLE 10
        classicModelsRepository.findTop3SalesPerEmployeeViaTableValuedFunctionAndCrossApply();   // EXAMPLE 11
        classicModelsRepository.outerApplyOfficeHasDepartments();                                // EXAMPLE 12
    }
}
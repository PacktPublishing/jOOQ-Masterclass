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

        classicModelsRepository.crossApplyOfficeHasDepartments();                              // EXAMPLE 1
        classicModelsRepository.outerApplyOfficeHasDepartments();                              // EXAMPLE 2
        classicModelsRepository.crossApplyEmployeeAvgSales();                                  // EXAMPLE 3 
        classicModelsRepository.crossApplyOfficeCityCountryHasDepartments();                   // EXAMPLE 4
        classicModelsRepository.crossApplyDepartmentUnnest();                                  // EXAMPLE 5
        classicModelsRepository.findTop3SalesPerEmployee();                                    // EXAMPLE 6
        classicModelsRepository.findTop3OrderedProductsIn2003();                               // EXAMPLE 7
        classicModelsRepository.findTop3SalesPerEmployeeViaTableValuedFunctionAndCrossApply(); // EXAMPLE 8                
    }
}

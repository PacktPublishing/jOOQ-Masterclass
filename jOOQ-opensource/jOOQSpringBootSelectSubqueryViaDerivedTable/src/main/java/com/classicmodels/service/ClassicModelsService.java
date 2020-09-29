package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    @Transactional(readOnly = true)
    public Object[][] numberOfSales() {

        return classicModelsRepository.numberOfSales();
    }
    
    @Transactional(readOnly = true)
    public Object[][] employeesAndNumberOfSales() {

        return classicModelsRepository.employeesAndNumberOfSales();
    }
    
    @Transactional(readOnly = true)
    public Object[][] employeesWithSalaryGeAvgPerOffice() {

        return classicModelsRepository.employeesWithSalaryGeAvgPerOffice();
    }
    
}

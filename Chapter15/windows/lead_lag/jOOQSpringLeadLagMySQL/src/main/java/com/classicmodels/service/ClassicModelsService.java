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

        classicModelsRepository.leadLagOrder();
        classicModelsRepository.leadSalaryByOffice();

        classicModelsRepository.lagYOY();
        classicModelsRepository.monthOverMonthGrowthRateSale();

        classicModelsRepository.leadLagSalary();     
        classicModelsRepository.firstRowInLastGroup();
        classicModelsRepository.employeeFunnel();
        
        classicModelsRepository.timeSeriesAnalysis();                
    }
}

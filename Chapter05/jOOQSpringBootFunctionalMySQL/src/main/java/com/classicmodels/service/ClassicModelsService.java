package com.classicmodels.service;

import com.classicmodels.pojo.EmployeeData;
import com.classicmodels.pojo.SaleStats;
import com.classicmodels.repository.ClassicModelsRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    @Transactional(readOnly = true)
    public SaleStats fetchSalesAndTotalSale() {

        return classicModelsRepository.findSalesAndTotalSale();
    }

    @Transactional(readOnly = true)
    public List<EmployeeData> fetchAllEmployee() {

        return classicModelsRepository.findAllEmployee();
    }
    
    public void z() {
        //classicModelsRepository.z();
    }
}

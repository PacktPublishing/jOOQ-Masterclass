package com.classicmodels.service;

import com.classicmodels.pojo.EmployeeData;
import com.classicmodels.pojo.SaleStats;
import com.classicmodels.repository.ClassicModelsRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    public List<EmployeeData> fetchAllEmployee() {

        return classicModelsRepository.findAllEmployee();
    }

    public List<Double> fetchAllSales() {

        return classicModelsRepository.findAllSales();
    }

    public SaleStats fetchSalesAndTotalSale() {

        return classicModelsRepository.findSalesAndTotalSale();
    }
}

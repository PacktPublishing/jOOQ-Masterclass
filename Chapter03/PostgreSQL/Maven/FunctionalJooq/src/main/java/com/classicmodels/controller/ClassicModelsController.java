package com.classicmodels.controller;

import com.classicmodels.pojo.EmployeeData;
import com.classicmodels.pojo.SaleStats;
import com.classicmodels.service.ClassicModelsService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassicModelsController {

    private final ClassicModelsService classicModelsService;

    public ClassicModelsController(ClassicModelsService classicModelsService) {
        this.classicModelsService = classicModelsService;
    }

    @GetMapping("/salesAndTotalSale")
    public SaleStats fetchSalesAndTotalSale() {

        return classicModelsService.fetchSalesAndTotalSale();
    }
    
    @GetMapping("/allEmployee")
    public List<EmployeeData> fetchAllEmployee() {
        
        return classicModelsService.fetchAllEmployee();
    }
}

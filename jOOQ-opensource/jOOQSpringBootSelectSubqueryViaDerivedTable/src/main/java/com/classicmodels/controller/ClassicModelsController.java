package com.classicmodels.controller;

import com.classicmodels.service.ClassicModelsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassicModelsController {

    private final ClassicModelsService classicModelsService;

    public ClassicModelsController(ClassicModelsService classicModelsService) {
        this.classicModelsService = classicModelsService;
    }

    @GetMapping("/numberOfSales")
    public Object[][] numberOfSales() {

        return classicModelsService.numberOfSales();
    }
    
    @GetMapping("/employeesAndNumberOfSales")
    public Object[][] employeesAndNumberOfSales() {

        return classicModelsService.employeesAndNumberOfSales();
    }        
    
    @GetMapping("/employeesWithSalaryGeAvgPerOffice")
    public Object[][] employeesWithSalaryGeAvgPerOffice() {

        return classicModelsService.employeesWithSalaryGeAvgPerOffice();
    }
}

package com.classicmodels.controller;

import com.classicmodels.pojo.EmployeeNoCntr;
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

    @GetMapping("/totalsales")
    public List<Object[]> fetchEmployeesWithTotalSalesByFiscalYear() {

        return classicModelsService.fetchEmployeesWithTotalSalesByFiscalYear();
    } 
    
    @GetMapping("/leastsalary")
    public List<EmployeeNoCntr> fetchEmployeesAndLeastSalary() {

        return classicModelsService.fetchEmployeesAndLeastSalary();
    }
    
    @GetMapping("/employeescsv")
    public String fetchEmployeesFirstNamesAsCsv() {

        return classicModelsService.fetchEmployeesFirstNamesAsCsv();
    }
}

package com.classicmodels.controller;

import com.classicmodels.pojo.EmployeeNoCntr;
import com.classicmodels.service.ClassicModelsService;
import java.util.List;
import jooq.generated.tables.interfaces.IEmployee;
import jooq.generated.tables.pojos.JooqEmployee;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    
    @GetMapping("/employeescntr")
    public List<JooqEmployee> fetchByJobTitleCntr(@RequestParam String jobTitle) {

        return classicModelsService.fetchByJobTitleCntr(jobTitle);
    }
    
    @GetMapping("/employeesinterfacejpql")
    public List<IEmployee> fetchByJobTitleJpql(@RequestParam String jobTitle) {
        
        return classicModelsService.fetchByJobTitleJpql(jobTitle);
    }
    
    @GetMapping("/employeesinterfacenative")
    public List<IEmployee> fetchByJobTitleNative(@RequestParam String jobTitle) {
        
        return classicModelsService.fetchByJobTitleNative(jobTitle);
    }
}

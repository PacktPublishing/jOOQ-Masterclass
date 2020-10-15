package com.classicmodels.controller;

import com.classicmodels.entity.Employee;
import com.classicmodels.pojo.EmployeeNoCntr;
import com.classicmodels.pojo.EmployeeCntr;
import com.classicmodels.service.ClassicModelsService;
import java.util.List;
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
    
    @GetMapping("/leastsalarycntr")
    public List<EmployeeCntr> fetchEmployeesAndLeastSalaryCntr() {

        return classicModelsService.findEmployeesAndLeastSalaryCntr();
    } 
    
    @GetMapping("/inoffice")
    public List<Employee> fetchEmployeeInCity(@RequestParam String city) {

        return classicModelsService.fetchEmployeeInCity(city);
    } 
    
    @GetMapping("/officeandemployee")
    public List<Object[]> fetchEmployeeAndOffices() {

        return classicModelsService.fetchEmployeeAndOffices();
    }    
    
    @GetMapping("/byjob")
    public List<Employee> fetchByJobTitle(@RequestParam String jobTitle) {

        return classicModelsService.fetchByJobTitle(jobTitle);
    }
}



























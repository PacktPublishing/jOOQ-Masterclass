package com.classicmodels.controller;

import com.classicmodels.entity.Employee;
import com.classicmodels.pojo.EmployeeDto;
import com.classicmodels.pojo.EmployeeDtoCntr;
import com.classicmodels.service.HRService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {
    
    private final HRService hrService;

    public MyController(HRService hrService) {
        this.hrService = hrService;
    }
    
    @GetMapping("/totalsales")
    public List<Object[]> fetchEmployeesWithTotalSalesByFiscalYear() {

        return hrService.fetchEmployeesWithTotalSalesByFiscalYear();
    } 
    
    @GetMapping("/leastsalary")
    public List<EmployeeDto> fetchEmployeesAndLeastSalary() {

        return hrService.fetchEmployeesAndLeastSalary();
    } 
    
    @GetMapping("/leastsalarycntr")
    public List<EmployeeDtoCntr> fetchEmployeesAndLeastSalaryCntr() {

        return hrService.findEmployeesAndLeastSalaryCntr();
    } 
    
    @GetMapping("/inoffice")
    public List<Employee> fetchEmployeeInCity(@RequestParam String city) {

        return hrService.fetchEmployeeInCity(city);
    } 
    
    @GetMapping("/byjob")
    public List<Employee> fetchByJobTitle(@RequestParam String jobTitle) {

        return hrService.fetchByJobTitle(jobTitle);
    }
}



























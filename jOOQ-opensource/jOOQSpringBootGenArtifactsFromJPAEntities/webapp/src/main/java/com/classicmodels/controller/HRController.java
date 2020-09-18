package com.classicmodels.controller;

import com.classicmodels.pojo.EmployeeDto;
import com.classicmodels.service.HRService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HRController {

    private final HRService hrService;

    public HRController(HRService hrService) {
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
    
    @GetMapping("/employeescsv")
    public String fetchEmployeesFirstNamesAsCsv() {

        return hrService.fetchEmployeesFirstNamesAsCsv();
    }
}

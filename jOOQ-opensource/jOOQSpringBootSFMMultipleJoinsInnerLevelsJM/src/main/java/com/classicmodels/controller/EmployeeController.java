package com.classicmodels.controller;

import com.classicmodels.pojo.EmployeeDTO;
import com.classicmodels.service.EmployeeService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employee")
    public List<EmployeeDTO> fetchEmployeeWithSalesAndCustomersByOfficeCode(@RequestParam String officeCode) {

        return employeeService.fetchEmployeeWithSalesAndCustomersByOfficeCode(officeCode);
    }
}

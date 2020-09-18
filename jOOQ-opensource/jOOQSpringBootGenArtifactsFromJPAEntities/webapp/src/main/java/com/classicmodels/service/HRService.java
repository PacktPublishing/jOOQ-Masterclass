package com.classicmodels.service;

import com.classicmodels.pojo.EmployeeDto;
import com.classicmodels.repository.EmployeeRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HRService {

    private final EmployeeRepository employeeRepository;

    public HRService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
    
    // uses jOOQ
    @Transactional(readOnly = true)
    public List<Object[]> fetchEmployeesWithTotalSalesByFiscalYear() {
        
        return employeeRepository.findEmployeesWithTotalSalesByFiscalYear();
    }
    
    // uses jOOQ
    @Transactional(readOnly = true)
    public List<EmployeeDto> fetchEmployeesAndLeastSalary() {

        return employeeRepository.findEmployeesAndLeastSalary();
    }
    
    // uses jOOQ
    public String fetchEmployeesFirstNamesAsCsv() {
        
        return employeeRepository.findEmployeesFirstNamesAsCsv();
    }
}

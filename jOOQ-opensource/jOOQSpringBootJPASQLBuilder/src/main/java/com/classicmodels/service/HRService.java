package com.classicmodels.service;

import com.classicmodels.entity.Employee;
import com.classicmodels.pojo.EmployeeDto;
import com.classicmodels.pojo.EmployeeDtoCntr;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.classicmodels.repository.EmployeeRepository;

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
    @Transactional(readOnly = true)
    public List<EmployeeDtoCntr> findEmployeesAndLeastSalaryCntr() {
        
        return employeeRepository.findEmployeesAndLeastSalaryCntr();
    }
    
    // uses jOOQ
    @Transactional
    public List<Employee> fetchEmployeeInCity(String city) {
        
        return employeeRepository.findEmployeeInCity(city);
    }
    
    // classic Spring Data JPA
    @Transactional
    public List<Employee> fetchByJobTitle(String jobTitle) {
        
        return employeeRepository.findByJobTitle(jobTitle);
    }
    
}

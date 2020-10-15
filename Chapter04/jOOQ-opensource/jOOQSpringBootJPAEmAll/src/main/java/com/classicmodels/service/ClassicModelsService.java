package com.classicmodels.service;

import com.classicmodels.entity.Employee;
import com.classicmodels.pojo.EmployeeNoCntr;
import com.classicmodels.pojo.EmployeeCntr;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.classicmodels.repository.EmployeeRepository;

@Service
public class ClassicModelsService {
    
    private final EmployeeRepository employeeRepository;

    public ClassicModelsService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
    
    // uses jOOQ
    @Transactional(readOnly = true)
    public List<Object[]> fetchEmployeesWithTotalSalesByFiscalYear() {
        
        return employeeRepository.findEmployeesWithTotalSalesByFiscalYear();
    }
    
    // uses jOOQ
    @Transactional(readOnly = true)
    public List<EmployeeNoCntr> fetchEmployeesAndLeastSalary() {
        
        return employeeRepository.findEmployeesAndLeastSalary();
    }
    
    // uses jOOQ
    @Transactional(readOnly = true)
    public List<EmployeeCntr> findEmployeesAndLeastSalaryCntr() {
        
        return employeeRepository.findEmployeesAndLeastSalaryCntr();
    }
    
    // uses jOOQ
    @Transactional
    public List<Employee> fetchEmployeeInCity(String city) {
        
        return employeeRepository.findEmployeeInCity(city);
    }
    
    @Transactional(readOnly = true)
    public List<Object[]> fetchEmployeeAndOffices() {
        
        return employeeRepository.findEmployeeAndOffices();
    }
    
    // classic Spring Data JPA
    @Transactional
    public List<Employee> fetchByJobTitle(String jobTitle) {
        
        return employeeRepository.findByJobTitle(jobTitle);
    }
    
}
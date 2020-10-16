package com.classicmodels.service;

import com.classicmodels.pojo.EmployeeNoCntr;
import com.classicmodels.repository.EmployeeRepository;
import java.util.List;
import jooq.generated.tables.interfaces.IEmployee;
import jooq.generated.tables.pojos.JooqEmployee;
import org.springframework.stereotype.Service;

@Service
public class ClassicModelsService {

    private final EmployeeRepository employeeRepository;

    public ClassicModelsService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
    
    // uses jOOQ    
    public List<Object[]> fetchEmployeesWithTotalSalesByFiscalYear() {
        
        return employeeRepository.findEmployeesWithTotalSalesByFiscalYear();
    }
    
    // uses jOOQ   
    public List<EmployeeNoCntr> fetchEmployeesAndLeastSalary() {

        return employeeRepository.findEmployeesAndLeastSalary();
    }
    
    // uses jOOQ
    public String fetchEmployeesFirstNamesAsCsv() {
        
        return employeeRepository.findEmployeesFirstNamesAsCsv();
    }
    
    // uses jOOQ generated POJO
    public List<JooqEmployee> fetchByJobTitleCntr(String jobTitle) {
        
        return employeeRepository.findByJobTitleCntr(jobTitle);
    }
    
    // uses jOOQ generated interface (JPQL query)
    public List<IEmployee> fetchByJobTitleJpql(String jobTitle) {
        
        return employeeRepository.findByJobTitleJpql(jobTitle);
    }
    
    // uses jOOQ generated interface (native query)
    public List<IEmployee> fetchByJobTitleNative(String jobTitle) {
        
        return employeeRepository.findByJobTitleNative(jobTitle);        
    }
}
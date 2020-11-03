package com.classicmodels.service;

import com.classicmodels.entity.Employee;
import com.classicmodels.pojo.EmployeeNoCntr;
import com.classicmodels.repository.EmployeeRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClassicModelsService {

    private final EmployeeRepository employeeRepository;

    public ClassicModelsService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // uses jOOQ   
    @Transactional(readOnly = true)
    public List<EmployeeNoCntr> fetchEmployeesAndLeastSalary() {

        return employeeRepository.findEmployeesAndLeastSalary();
    }

    // uses Spring Data JPA
    @Transactional
    public List<com.classicmodels.entity.Employee> fetchByJobTitle(String jobTitle) {

        return employeeRepository.findByJobTitle(jobTitle);
    }
    
    public List<jooq.generated.tables.pojos.Employee> fff() {
        return employeeRepository.fetchBySalary(50000);
    }
    
    public void x() {
        
        jooq.generated.tables.pojos.Employee xx = employeeRepository.fetchOneByEmployeeNumber(1002L);
        xx.setFirstName("ssssss");
        employeeRepository.update(xx);
    }
}

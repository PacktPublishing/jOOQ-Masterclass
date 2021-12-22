package com.classicmodels.service;

import com.classicmodels.entity.Employee;
import com.classicmodels.pojo.EmployeeNoCntr;
import com.classicmodels.repository.EmployeeRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ClassicModelsService {

    private final EmployeeRepository employeeRepository;

    public ClassicModelsService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // uses jOOQ       
    public List<EmployeeNoCntr> fetchEmployeesAndLeastSalary() {

        return employeeRepository.findEmployeesAndLeastSalary();
    }

    // uses Spring Data JPA    
    public List<Employee> fetchByJobTitle(String jobTitle) {

        return employeeRepository.findByJobTitle(jobTitle);
    }
}

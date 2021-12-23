package com.classicmodels.service

import com.classicmodels.entity.Employee
import com.classicmodels.pojo.EmployeeNoCntr
import com.classicmodels.repository.EmployeeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ClassicModelsService(private val employeeRepository: EmployeeRepository) {

    // uses jOOQ       
    fun fetchEmployeesAndLeastSalary(): MutableList<EmployeeNoCntr> {
        return employeeRepository.findEmployeesAndLeastSalary()
    }

    // uses Spring Data JPA    
    fun fetchByJobTitle(jobTitle: String): MutableList<Employee> {
        return employeeRepository.findByJobTitle(jobTitle)
    }
}
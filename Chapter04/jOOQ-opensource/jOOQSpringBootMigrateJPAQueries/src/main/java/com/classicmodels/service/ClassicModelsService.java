package com.classicmodels.service;

import com.classicmodels.entity.Employee;
import com.classicmodels.pojo.EmployeeCntr;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.classicmodels.repository.EmployeeRepository;
import com.classicmodels.pojo.EmployeeProjection1;
import com.classicmodels.pojo.EmployeeProjection2;
import java.util.Random;
import jooq.generated.tables.interfaces.IEmployee;
import jooq.generated.tables.pojos.JooqEmployee;

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
    public List<EmployeeProjection1> fetchEmployeesAndLeastSalary() {

        return employeeRepository.findEmployeesAndLeastSalary();
    }

    // uses jOOQ
    @Transactional(readOnly = true)
    public List<EmployeeCntr> findEmployeesAndLeastSalaryCntr() {

        return employeeRepository.findEmployeesAndLeastSalaryCntr();
    }

    // uses jOOQ
    @Transactional(readOnly = true)
    public List<EmployeeProjection2> fetchFirst5ByOrderBySalaryDesc() {

        return employeeRepository.findFirst5ByOrderBySalaryDesc();
    }

    // uses jOOQ
    @Transactional
    public List<Employee> fetchEmployeeInCity(String city) {

        List<Employee> result = employeeRepository.findEmployeeInCity(city);

        // this will make Hibernate to trigger an UPDATE
        result.get(0).setSalary(50000 + new Random().nextInt(50000));

        return result;
    }

    // uses jOOQ
    @Transactional
    public List<Employee> fetchEmployeeSalaryInRange(int start, int end) {

        return employeeRepository.findEmployeeSalaryInRange(start, end);
    }

    // uses jOOQ generated POJO
    @Transactional(readOnly = true)
    public List<JooqEmployee> fetchByJobTitleCntr(String jobTitle) {

        return employeeRepository.findByJobTitleCntr(jobTitle);
    }

    // uses jOOQ generated interface (JPQL query)
    @Transactional(readOnly = true)
    public List<IEmployee> fetchByJobTitleJpql(String jobTitle) {

        return employeeRepository.findByJobTitleJpql(jobTitle);
    }

    // uses jOOQ generated interface (native query)
    @Transactional(readOnly = true)
    public List<IEmployee> fetchByJobTitleNative(String jobTitle) {

        return employeeRepository.findByJobTitleNative(jobTitle);
    }
}

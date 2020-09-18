package com.classicmodels.service;

import com.classicmodels.pojo.EmployeeDTO;
import com.classicmodels.repository.EmployeeRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional(readOnly = true)
    public List<EmployeeDTO> fetchEmployeeWithSalesAndCustomersByOfficeCode(String officeCode) {

        return employeeRepository.findEmployeeWithSalesAndCustomersByOfficeCode(officeCode);
    }

}

package com.classicmodels.repository;

import com.classicmodels.entity.Employee;
import com.classicmodels.pojo.EmployeeDto;
import com.classicmodels.pojo.EmployeeDtoCntr;
import java.util.List;

public interface QueryRepository {
    
    public List<Object[]> findEmployeesWithTotalSalesByFiscalYear();    
    public List<EmployeeDto> findEmployeesAndLeastSalary();
    public List<EmployeeDtoCntr> findEmployeesAndLeastSalaryCntr();
    public List<Employee> findEmployeeInCity(String city);
}

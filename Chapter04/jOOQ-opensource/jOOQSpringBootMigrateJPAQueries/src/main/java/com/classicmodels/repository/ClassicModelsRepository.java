package com.classicmodels.repository;

import com.classicmodels.entity.Employee;
import com.classicmodels.pojo.EmployeeCntr;
import java.util.List;
import com.classicmodels.pojo.EmployeeSlim;
import com.classicmodels.pojo.EmployeeLeastSalary;

public interface ClassicModelsRepository {

    public List<Object[]> findEmployeesWithTotalSalesByFiscalYear();

    public List<EmployeeLeastSalary> findEmployeesAndLeastSalary();

    public List<EmployeeCntr> findEmployeesAndLeastSalaryCntr();

    public List<EmployeeSlim> findFirst5ByOrderBySalaryDesc();

    public List<Employee> findEmployeeInCity(String city); 
    
    public List<Employee> findEmployeeSalaryInRange(int start, int end);
}

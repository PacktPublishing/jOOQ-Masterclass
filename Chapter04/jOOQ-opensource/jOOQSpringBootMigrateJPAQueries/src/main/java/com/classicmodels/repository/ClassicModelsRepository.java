package com.classicmodels.repository;

import com.classicmodels.entity.Employee;
import com.classicmodels.pojo.EmployeeCntr;
import java.util.List;
import com.classicmodels.pojo.EmployeeProjection1;
import com.classicmodels.pojo.EmployeeProjection2;

public interface ClassicModelsRepository {

    public List<Object[]> findEmployeesWithTotalSalesByFiscalYear();

    public List<EmployeeProjection1> findEmployeesAndLeastSalary();

    public List<EmployeeCntr> findEmployeesAndLeastSalaryCntr();

    public List<EmployeeProjection2> findFirst5ByOrderBySalaryDesc();

    public List<Employee> findEmployeeInCity(String city); 
    
    public List<Employee> findEmployeeSalaryInRange(int start, int end);
}

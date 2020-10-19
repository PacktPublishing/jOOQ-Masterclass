package com.classicmodels.repository;

import com.classicmodels.entity.Employee;
import com.classicmodels.pojo.EmployeeNoCntr;
import com.classicmodels.pojo.EmployeeCntr;
import java.util.List;

public interface ClassicModelsRepository {
    
    public List<Object[]> findEmployeesWithTotalSalesByFiscalYear();    
    public List<EmployeeNoCntr> findEmployeesAndLeastSalary();
    public List<EmployeeCntr> findEmployeesAndLeastSalaryCntr();
    public List<Employee> findEmployeeInCity(String city);
    public List<Employee> findEmployeeBySalary(int salary);
    public List<Object[]> findEmployeeAndOffices();
}

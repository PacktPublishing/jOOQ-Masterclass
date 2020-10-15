package com.classicmodels.repository;

import com.classicmodels.pojo.EmployeeNoCntr;
import com.classicmodels.pojo.EmployeeCntr;
import java.util.List;

public interface ClassicModelsRepository {
    
    public List<Object[]> findEmployeesWithTotalSalesByFiscalYear();    
    public List<EmployeeNoCntr> findEmployeesAndLeastSalary();
    public List<EmployeeCntr> findEmployeesAndLeastSalaryCntr();    
}

package com.classicmodels.repository;

import com.classicmodels.pojo.EmployeeNoCntr;
import java.util.List;

public interface ClassicModelsRepository {

    public List<Object[]> findEmployeesWithTotalSalesByFiscalYear();
    public List<EmployeeNoCntr> findEmployeesAndLeastSalary();
    public String findEmployeesFirstNamesAsCsv();
}

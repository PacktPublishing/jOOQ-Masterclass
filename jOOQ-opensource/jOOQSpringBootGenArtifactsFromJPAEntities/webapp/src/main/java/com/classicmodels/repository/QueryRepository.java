package com.classicmodels.repository;

import com.classicmodels.pojo.EmployeeDto;
import java.util.List;

public interface QueryRepository {

    public List<Object[]> findEmployeesWithTotalSalesByFiscalYear();
    public List<EmployeeDto> findEmployeesAndLeastSalary();
    public String findEmployeesFirstNamesAsCsv();
}

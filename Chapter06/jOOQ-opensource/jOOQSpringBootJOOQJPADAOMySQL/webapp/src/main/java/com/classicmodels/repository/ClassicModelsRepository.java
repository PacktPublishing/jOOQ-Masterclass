package com.classicmodels.repository;

import com.classicmodels.pojo.EmployeeNoCntr;
import java.util.List;
import jooq.generated.tables.pojos.Customer;
import org.jooq.exception.DataAccessException;

public interface ClassicModelsRepository {  
    
    public List<EmployeeNoCntr> findEmployeesAndLeastSalary();   
}

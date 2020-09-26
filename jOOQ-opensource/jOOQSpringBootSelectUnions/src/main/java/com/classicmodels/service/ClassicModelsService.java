package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import java.util.List;
import jooq.generated.tables.pojos.Employee;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    @Transactional(readOnly = true)
    public Object[][] unionEmployeeAndCustomerNames() {

         return classicModelsRepository.unionEmployeeAndCustomerNames();
    }        
    
    @Transactional(readOnly = true)
    public Object[][] unionEmployeeAndCustomerNamesConcatColumns() {
        
        return classicModelsRepository.unionEmployeeAndCustomerNamesConcatColumns();
    }
    
    @Transactional(readOnly = true)
    public Object[][] unionEmployeeAndCustomerNamesDifferentiate() {
        
        return classicModelsRepository.unionEmployeeAndCustomerNamesDifferentiate();
    }
    
    @Transactional(readOnly = true)
    public Object[][] unionEmployeeAndCustomerNamesOrderBy() {
        
        return classicModelsRepository.unionEmployeeAndCustomerNamesOrderBy();
    }
    
    @Transactional(readOnly = true)
    public List<Employee> unionEmployeeSmallestAndHighestSalary() {
        
        return classicModelsRepository.unionEmployeeSmallestAndHighestSalary();
    }
    
    @Transactional(readOnly = true)
    public Object[][] unionAllOfficeCustomerCityAndCountry() {
        
        return classicModelsRepository.unionAllOfficeCustomerCityAndCountry();
    }
}

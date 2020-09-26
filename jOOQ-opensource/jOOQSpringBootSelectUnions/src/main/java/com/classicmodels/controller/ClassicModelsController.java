package com.classicmodels.controller;

import com.classicmodels.service.ClassicModelsService;
import java.util.List;
import jooq.generated.tables.pojos.Employee;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassicModelsController {

    private final ClassicModelsService classicModelsService;

    public ClassicModelsController(ClassicModelsService classicModelsService) {
        this.classicModelsService = classicModelsService;
    }
    
    @GetMapping("/names")
    public Object[][] unionEmployeeAndCustomerNames() {

        return classicModelsService.unionEmployeeAndCustomerNames();
    }        
    
    @GetMapping("/namesConcat")
    public Object[][] unionEmployeeAndCustomerNamesConcatColumns() {

        return classicModelsService.unionEmployeeAndCustomerNamesConcatColumns();
    }
    
    @GetMapping("/namesDifferentiate")
    public Object[][] unionEmployeeAndCustomerNamesDifferentiate() {

        return classicModelsService.unionEmployeeAndCustomerNamesDifferentiate();
    }    
    
    @GetMapping("/namesOrdered")
    public Object[][] unionEmployeeAndCustomerNamesOrderBy() {

        return classicModelsService.unionEmployeeAndCustomerNamesOrderBy();
    }
    
    @GetMapping("/employeeSalary")
    public List<Employee> unionEmployeeSmallestAndHighestSalary() {

        return classicModelsService.unionEmployeeSmallestAndHighestSalary();
    }        
    
    @GetMapping("/citycountry")
    public Object[][] unionAllOfficeCustomerCityAndCountry() {

        return classicModelsService.unionAllOfficeCustomerCityAndCountry();
    }
}

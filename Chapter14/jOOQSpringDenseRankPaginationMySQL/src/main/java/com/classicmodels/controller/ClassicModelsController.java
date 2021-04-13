package com.classicmodels.controller;

import com.classicmodels.service.ClassicModelsService;
import java.util.List;
import java.util.Map;
import jooq.generated.tables.pojos.Employee;
import jooq.generated.tables.pojos.Office;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassicModelsController {

    private final ClassicModelsService classicModelsService;

    public ClassicModelsController(ClassicModelsService classicModelsService) {
        this.classicModelsService = classicModelsService;
    }
    
    @GetMapping("/officeWithEmployeesOffset/{page}/{size}")
    public Map<Office, List<Employee>> fetchOfficeWithEmployeeOffset(
            @PathVariable(name = "page") int page, @PathVariable(name = "size") int size) {
        
        return classicModelsService.loadOfficeWithEmployeeOffset(page, size);
    }
    
    @GetMapping("/officeWithEmployeesSeek/{officeCode}/{size}")
    public Map<Office, List<Employee>> loadOfficeWithEmployeeSeek(
            @PathVariable(name = "officeCode") String officeCode, @PathVariable(name = "size") int size) {
        
        return classicModelsService.loadOfficeWithEmployeeSeek(officeCode, size);
    }
        
    @GetMapping("/officeWithEmployeesDR/{start}/{end}")
    public Map<Office, List<Employee>> loadOfficeWithEmployeeDR(
            @PathVariable(name = "start") int start, @PathVariable(name = "end") int end) {

        return classicModelsService.loadOfficeWithEmployeeDR(start, end);
    } 
}

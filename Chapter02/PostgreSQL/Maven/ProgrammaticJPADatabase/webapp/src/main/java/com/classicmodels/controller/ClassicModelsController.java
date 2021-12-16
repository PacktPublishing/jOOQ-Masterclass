package com.classicmodels.controller;

import com.classicmodels.entity.Employee;
import com.classicmodels.pojo.EmployeeNoCntr;
import com.classicmodels.service.ClassicModelsService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassicModelsController {

    private final ClassicModelsService classicModelsService;

    public ClassicModelsController(ClassicModelsService classicModelsService) {
        this.classicModelsService = classicModelsService;
    }

    @GetMapping("/leastsalary")
    public List<EmployeeNoCntr> fetchEmployeesAndLeastSalary() {

        return classicModelsService.fetchEmployeesAndLeastSalary();
    }

    @GetMapping("/employeescsv")
    public List<Employee> fetchByJobTitle(
            @RequestParam(name = "jobTitle") String jobTitle) {

        return classicModelsService.fetchByJobTitle(jobTitle);
    }
}

package com.classicmodels.controller

import com.classicmodels.entity.Employee
import com.classicmodels.pojo.EmployeeNoCntr
import com.classicmodels.service.ClassicModelsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
public class ClassicModelsController(private val classicModelsService: ClassicModelsService) {

    @GetMapping("/leastsalary")
    fun fetchEmployeesAndLeastSalary(): MutableList<EmployeeNoCntr> {
        return classicModelsService.fetchEmployeesAndLeastSalary()
    }

    @GetMapping("/employeescsv")
    fun fetchByJobTitle(
            @RequestParam(name = "jobTitle") jobTitle: String): MutableList<Employee> {
        return classicModelsService.fetchByJobTitle(jobTitle)
    }
}

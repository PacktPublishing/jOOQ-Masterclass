package com.classicmodels.controller;

import com.classicmodels.pojo.SimpleEmployee;
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

    @GetMapping("/employees")
    public List<SimpleEmployee> fetchEmployeeWithSalesAndCustomersByOfficeCode(
            @RequestParam("officeCode") String officeCode) {

        return classicModelsService.fetchEmployeeWithSalesAndCustomersByOfficeCode(officeCode);
    }
}

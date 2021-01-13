package com.classicmodels.controller;

import com.classicmodels.pojo.SimpleCustomer;
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

    @GetMapping("/customers")
    public List<SimpleCustomer> fetchCustomerByCreditLimit(
            @RequestParam("creditLimit") float creditLimit) {

        return classicModelsService.fetchCustomerByCreditLimit(creditLimit);
    }
}

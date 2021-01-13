package com.classicmodels.controller;

import com.classicmodels.pojo.SimpleCustomer;
import com.classicmodels.service.ClassicModelService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassicModelController {

    private final ClassicModelService classicModelService;

    public ClassicModelController(ClassicModelService classicModelService) {
        this.classicModelService = classicModelService;
    }

    @GetMapping("/customers")
    public List<SimpleCustomer> fetchCustomerByCreditLimit(
            @RequestParam("creditLimit") float creditLimit) {

        return classicModelService.fetchCustomerByCreditLimit(creditLimit);
    }
}

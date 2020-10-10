package com.classicmodels.controller;

import com.classicmodels.pojo.DelayedPayment;
import com.classicmodels.pojo.Manager;
import com.classicmodels.pojo.CustomerCachingDate;
import com.classicmodels.service.ClassicModelsService;
import java.time.LocalDate;
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
    
    @GetMapping("/manager")
    public Manager fetchManager(@RequestParam Long managerId) {

        return classicModelsService.fetchManager(managerId);
    }                 

    @GetMapping("/customercachingdate")
    public List<CustomerCachingDate> fetchCustomerCachingDate() {

        return classicModelsService.fetchCustomerCachingDate();
    }        
    
    @GetMapping("/delayedpayments")
    public List<DelayedPayment> fetchDelayedPayments(
            @RequestParam String sd, @RequestParam String ed) {

        return classicModelsService.fetchDelayedPayments(
                LocalDate.parse(sd), LocalDate.parse(ed));
    }
}
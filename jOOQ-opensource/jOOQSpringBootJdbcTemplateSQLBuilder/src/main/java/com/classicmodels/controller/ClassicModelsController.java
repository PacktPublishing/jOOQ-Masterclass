package com.classicmodels.controller;

import com.classicmodels.pojo.DelayedPayment;
import com.classicmodels.pojo.Order;
import com.classicmodels.pojo.OrderAndNextOrderDate;
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
    
    @GetMapping("/order")
    public Order fetchOrder(@RequestParam Long orderId) {

        return classicModelsService.fetchOrder(orderId);
    }                 

    @GetMapping("/orderandnextorderdate")
    public List<OrderAndNextOrderDate> fetchOrderAndNextOrderDate() {

        return classicModelsService.fetchOrderAndNextOrderDate();
    }        
    
    @GetMapping("/delayedpayments")
    public List<DelayedPayment> fetchDelayedPayments(
            @RequestParam String sd, @RequestParam String ed) {

        return classicModelsService.fetchDelayedPayments(
                LocalDate.parse(sd), LocalDate.parse(ed));
    }
}
package com.classicmodels.controller;

import com.classicmodels.pojo.DelayedPayment;
import com.classicmodels.pojo.Order;
import com.classicmodels.pojo.OrderAndNextOrderDate;
import com.classicmodels.service.OrderPaymentService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderPaymentController {

    private final OrderPaymentService paymentService;

    public OrderPaymentController(OrderPaymentService paymentService) {
        this.paymentService = paymentService;
    }
    
    @GetMapping("/order")
    public Order fetchOrder(@RequestParam Long orderId) {

        return paymentService.fetchOrder(orderId);
    } 
    
    @GetMapping("/delayedpayments")
    public List<DelayedPayment> fetchDelayedPayments(
            @RequestParam String sd, @RequestParam String ed) {

        return paymentService.fetchDelayedPayments(
                LocalDate.parse(sd), LocalDate.parse(ed));
    }        

    @GetMapping("/orderandnextorderdate")
    public List<OrderAndNextOrderDate> fetchOrderAndNextOrderDate() {

        return paymentService.fetchOrderAndNextOrderDate();
    }        
    
}

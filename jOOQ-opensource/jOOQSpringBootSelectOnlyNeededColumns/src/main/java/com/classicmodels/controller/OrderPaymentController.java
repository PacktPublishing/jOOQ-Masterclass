package com.classicmodels.controller;

import com.classicmodels.pojo.Order;
import com.classicmodels.service.OrderPaymentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderPaymentController {

    private final OrderPaymentService paymentService;

    public OrderPaymentController(OrderPaymentService paymentService) {
        this.paymentService = paymentService;
    }
    /*
    @GetMapping("/order1")
    public Order fetchOrderTooMuchFields(@RequestParam Long orderId) {

        return paymentService.fetchOrderTooMuchFields(orderId);
    }        
    
    @GetMapping("/order2")
    public Order fetchOrderExplicitFields(@RequestParam Long orderId) {

        return paymentService.fetchOrderExplicitFields(orderId);
    }
*/
}

package com.classicmodels.controller;

import com.classicmodels.service.OrderPaymentService;
import java.util.List;
import java.util.Map;
import jooq.generated.tables.pojos.Order;
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
    
    @GetMapping("/ordersgroup")
    public Map<Long, List<Order>> fetchAndGroupOrdersByCustomerId() {

        return paymentService.fetchAndGroupOrdersByCustomerId();
    }
    
    @GetMapping("/customerorders")
    public List<Order> fetchCustomerOrders(Long customerNumber) {
     
        return paymentService.fetchCustomerOrders(customerNumber);
    }
}

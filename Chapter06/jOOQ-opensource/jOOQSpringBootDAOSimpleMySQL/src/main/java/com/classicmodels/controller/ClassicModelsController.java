package com.classicmodels.controller;

import com.classicmodels.service.CustomerOrderManagementService;
import java.util.List;
import jooq.generated.tables.pojos.Customer;
import jooq.generated.tables.pojos.Order;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassicModelsController {

    private final CustomerOrderManagementService customerOrderManagementService;

    public ClassicModelsController(CustomerOrderManagementService customerOrderManagementService) {
        this.customerOrderManagementService = customerOrderManagementService;
    }

    @GetMapping("/customersOrderedByCreditLimit")
    public List<Customer> fetchCustomersOrderedByCreditLimit() {

        return customerOrderManagementService.fetchCustomersOrderedByCreditLimit();
    }
    
    @GetMapping("/customerByPhone")
    public List<Customer> findCustomerByPhone(@RequestParam String phone) {
        
        return customerOrderManagementService.fetchCustomerByPhone(phone);
    }

    @GetMapping("/orderStatus")
    public List<String> findOrderStatus() {
        
        return customerOrderManagementService.fetchOrderStatus();
    }
    
    @GetMapping("/orderById")
    public Order findOrderById(@RequestParam Long id) {
        
        return customerOrderManagementService.fetchOrderById(id);
    }
}

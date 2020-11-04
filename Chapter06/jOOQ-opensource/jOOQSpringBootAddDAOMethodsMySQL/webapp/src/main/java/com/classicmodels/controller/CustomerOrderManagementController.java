package com.classicmodels.controller;

import com.classicmodels.service.CustomerOrderManagementService;
import java.time.LocalDate;
import java.util.List;
import jooq.generated.tables.pojos.Customer;
import jooq.generated.tables.pojos.Order;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerOrderManagementController {

    private final CustomerOrderManagementService customerOrderManagementService;

    public CustomerOrderManagementController(CustomerOrderManagementService customerOrderManagementService) {
        this.customerOrderManagementService = customerOrderManagementService;
    }

    @GetMapping("/first5Customers")
    public List<Customer> fetchFirstNCustomers(@RequestParam int n) {

        return customerOrderManagementService.fetchFirstNCustomers(n);
    }

    @GetMapping("/statusAndOrderDate")
    public List<Order> fetchStatusAndOrderDate(
            @RequestParam String status, @RequestParam String orderDate) {

        return customerOrderManagementService.fetchStatusAndOrderDate(status, LocalDate.parse(orderDate));
    }

}

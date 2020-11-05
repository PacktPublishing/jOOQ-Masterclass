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

    @GetMapping("/customerOrderGtCreditLimit")
    public List<Customer> fetchCustomerOrderGtCreditLimit(@RequestParam int cl) {

        return customerOrderManagementService.fetchCustomerOrderGtCreditLimit(cl);
    }

    @GetMapping("/customerByPhone")
    public List<Customer> fetchCustomerByPhone(@RequestParam String phone) {

        return customerOrderManagementService.fetchCustomerByPhone(phone);
    }

    @GetMapping("/orderStatuses")
    public List<String> fetchOrderStatuses() {

        return customerOrderManagementService.fetchOrderStatuses();
    }

    @GetMapping("/orderByShippedDate")
    public List<Order> fetchOrderByShippedDate(@RequestParam String date) {

        return customerOrderManagementService.fetchOrderByShippedDate(LocalDate.parse(date));
    }
}

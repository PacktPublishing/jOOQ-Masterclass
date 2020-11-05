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

    @GetMapping("/customerAscGtCreditLimit")
    public List<Customer> fetchCustomerAscGtCreditLimit(@RequestParam int cl) {

        return customerOrderManagementService.fetchCustomerAscGtCreditLimit(cl);
    }

    @GetMapping("/customerByPhone")
    public List<Customer> fetchCustomerByPhone(@RequestParam String phone) {

        return customerOrderManagementService.fetchCustomerByPhone(phone);
    }

    @GetMapping("/orderDescByDate")
    public List<Order> fetchOrderDescByDate() {

        return customerOrderManagementService.fetchOrderDescByDate();
    }

    @GetMapping("/orderBetweenDate")
    public List<Order> fetchOrderBetweenDate(@RequestParam String sd, @RequestParam String ed) {

        return customerOrderManagementService.fetchOrderBetweenDate(
                LocalDate.parse(sd), LocalDate.parse(ed));
    }
}

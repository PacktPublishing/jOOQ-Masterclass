package com.classicmodels.controller;

import com.classicmodels.pojo.ManagerDTO;
import com.classicmodels.service.CustomerService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/manageroffice")
    public List<ManagerDTO> fetchCustomerByCreditLimit(@RequestParam float creditLimit) {

        return customerService.fetchCustomerByCreditLimit(creditLimit);
    }
}

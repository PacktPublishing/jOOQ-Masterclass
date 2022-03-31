package com.classicmodels.controller;

import com.classicmodels.service.OrderManagementService;
import java.time.LocalDate;
import java.util.List;
import jooq.generated.tables.pojos.Order;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderManagementController {

    private final OrderManagementService customerOrderManagementService;

    public OrderManagementController(OrderManagementService customerOrderManagementService) {
        this.customerOrderManagementService = customerOrderManagementService;
    }

    @GetMapping("/limitedTo")
    public List<Order> fetchLimitedTo(@RequestParam int limit) {

        return customerOrderManagementService.fetchLimitedTo(limit);
    }

    @GetMapping("/orderByStatusAndOrderDate")
    public List<Order> fetchOrderByStatusAndOrderDate(
            @RequestParam String status, @RequestParam String orderDate) {

        return customerOrderManagementService.fetchOrderByStatusAndOrderDate(
                status, LocalDate.parse(orderDate));
    }

}

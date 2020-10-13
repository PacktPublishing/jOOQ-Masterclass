package com.classicmodels.controller;

import com.classicmodels.pojo.CustomerAndOrder;
import com.classicmodels.service.ClassicModelsService;
import java.time.LocalDate;
import java.util.List;
import jooq.generated.tables.pojos.Manager;
import jooq.generated.tables.pojos.Order;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassicModelsController {

    private final ClassicModelsService classicModelsService;

    public ClassicModelsController(ClassicModelsService classicModelsService) {
        this.classicModelsService = classicModelsService;
    }

    @GetMapping("/manager")
    public Manager fetchManager(@RequestParam Long managerId) {

        return classicModelsService.fetchManager(managerId);
    }

    @GetMapping("/ordersbyrequireddate")
    public List<Order> fetchOrdersByRequiredDate(
            @RequestParam String startDate, @RequestParam String endDate) {

        return classicModelsService.fetchOrdersByRequiredDate(
                LocalDate.parse(startDate), LocalDate.parse(endDate));
    }

    @GetMapping("/customersandorders")
    public List<CustomerAndOrder> fetchCustomersAndOrders() {

        return classicModelsService.fetchCustomersAndOrders();
    }

}
package com.classicmodels.controller;

import com.classicmodels.pojo.CustomerAndOrder;
import com.classicmodels.pojo.Office;
import com.classicmodels.pojo.Order;
import com.classicmodels.service.ClassicModelsService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassicModelsController {

    private final ClassicModelsService classicModelsService;

    public ClassicModelsController(ClassicModelsService classicModelsService) {
        this.classicModelsService = classicModelsService;
    }

    @GetMapping("/officesInTerritory")
    public List<Office> fetchOfficesInTerritory(@RequestParam String territory) {

        return classicModelsService.fetchOfficesInTerritory(territory);
    }

    @GetMapping("/ordersByRequiredDate")
    public List<Order> fetchOrdersByRequiredDate(
            @RequestParam String startDate, @RequestParam String endDate) {

        return classicModelsService.fetchOrdersByRequiredDate(
                LocalDate.parse(startDate), LocalDate.parse(endDate));
    }

    @GetMapping("/customersAndOrders")
    public List<CustomerAndOrder> fetchCustomersAndOrders() {

        return classicModelsService.fetchCustomersAndOrders();
    }

}
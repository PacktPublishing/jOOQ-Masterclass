package com.classicmodels.controller;

import com.classicmodels.pojo.CustomerAndOrder;
import com.classicmodels.service.ClassicModelsService;
import java.time.LocalDate;
import java.util.List;
import jooq.generated.tables.pojos.JooqManager;
import jooq.generated.tables.pojos.JooqOrder;
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
    public JooqManager fetchManager(@RequestParam Long managerId) {

        return classicModelsService.fetchManager(managerId);
    }

    @GetMapping("/ordersbyrequireddate")
    public List<JooqOrder> fetchOrdersByRequiredDate(
            @RequestParam String startDate, @RequestParam String endDate) {

        return classicModelsService.fetchOrdersByRequiredDate(
                LocalDate.parse(startDate), LocalDate.parse(endDate));
    }

    @GetMapping("/customersandorders")
    public List<CustomerAndOrder> fetchCustomersAndOrders() {

        return classicModelsService.fetchCustomersAndOrders();
    }

}
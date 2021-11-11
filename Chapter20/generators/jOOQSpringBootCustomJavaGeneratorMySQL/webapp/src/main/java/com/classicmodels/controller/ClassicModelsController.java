package com.classicmodels.controller;

import com.classicmodels.service.ClassicModelsService;
import java.time.LocalDate;
import java.util.List;
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

    @GetMapping("/limitedTo")
    public List<Order> fetchLimitedTo(@RequestParam int limit) {

        return classicModelsService.fetchLimitedTo(limit);
    }

    @GetMapping("/orderByStatusAndOrderDate")
    public List<Order> fetchOrderByStatusAndOrderDate(
            @RequestParam String status, @RequestParam String orderDate) {

        return classicModelsService.fetchOrderByStatusAndOrderDate(
                status, LocalDate.parse(orderDate));
    }

}

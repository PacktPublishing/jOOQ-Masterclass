package com.classicmodels.controller;

import com.classicmodels.service.ClassicModelsService;
import java.math.BigDecimal;
import java.util.List;
import jooq.generated.tables.pojos.Orderdetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassicModelsController {

    private final ClassicModelsService classicModelsService;

    public ClassicModelsController(ClassicModelsService classicModelsService) {
        this.classicModelsService = classicModelsService;
    }

    @GetMapping("/orderdetail/{orderdetailId}/{size}")
    public List<Orderdetail> fetchOrderdetailPageAsc(
            @PathVariable("orderdetailId") int orderdetailId, @PathVariable("size") int size,
            @RequestParam(name = "priceEach", required = false) BigDecimal priceEach,
            @RequestParam(name = "quantityOrdered", required = false) Integer quantityOrdered) {

        return classicModelsService.loadOrderdetailPageAsc(orderdetailId, size, priceEach, quantityOrdered);
    }
}

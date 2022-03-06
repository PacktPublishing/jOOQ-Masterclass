package com.classicmodels.controller;

import com.classicmodels.service.ClassicModelsService;
import java.util.List;
import jooq.generated.tables.pojos.Orderdetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassicModelsController {

    private final ClassicModelsService classicModelsService;

    public ClassicModelsController(ClassicModelsService classicModelsService) {
        this.classicModelsService = classicModelsService;
    }

    @GetMapping("/orderdetail/{orderdetailId}/{size}")
    public List<Orderdetail> loadOrderdetailPageAsc(@PathVariable(name = "orderdetailId") int orderdetailId,
            @PathVariable(name = "size") int size) {

        return classicModelsService.loadOrderdetailPageAsc(orderdetailId, size);
    }
}

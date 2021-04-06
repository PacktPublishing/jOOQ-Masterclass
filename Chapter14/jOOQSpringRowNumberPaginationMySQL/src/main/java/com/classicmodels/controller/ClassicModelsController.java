package com.classicmodels.controller;

import com.classicmodels.service.ClassicModelsService;
import java.util.List;
import jooq.generated.tables.pojos.ProductMaster;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassicModelsController {

    private final ClassicModelsService classicModelsService;

    public ClassicModelsController(ClassicModelsService classicModelsService) {
        this.classicModelsService = classicModelsService;
    }

    @GetMapping("/products/{start}/{end}")
    public List<ProductMaster> loadProductMasterPage(
            @PathVariable(name = "start") int start, @PathVariable(name = "end") int end) {

        return classicModelsService.loadProductMasterPage(start, end);
    }
}

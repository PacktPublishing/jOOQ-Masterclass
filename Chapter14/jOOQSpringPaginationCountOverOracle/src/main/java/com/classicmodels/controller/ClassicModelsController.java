package com.classicmodels.controller;

import com.classicmodels.service.ClassicModelsService;
import jooq.generated.tables.pojos.Product;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassicModelsController {

    private final ClassicModelsService classicModelsService;

    public ClassicModelsController(ClassicModelsService classicModelsService) {
        this.classicModelsService = classicModelsService;
    }

    @GetMapping("/products/{page}/{size}")
    public Page<Product> loadProducts(@PathVariable(name = "page") int page, @PathVariable(name = "size") int size) {

        return classicModelsService.loadProducts(page, size);
    }
}

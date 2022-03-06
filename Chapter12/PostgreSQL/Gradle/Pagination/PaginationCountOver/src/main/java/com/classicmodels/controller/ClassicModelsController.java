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

    @GetMapping("/products1/{page}/{size}")
    public Page<Product> loadProductsExtraSelectCount(@PathVariable(name = "page") int page, @PathVariable(name = "size") int size) {

        return classicModelsService.loadProductsExtraSelectCount(page, size);
    }

    @GetMapping("/products2/{page}/{size}")
    public Page<Product> loadProductsWithoutExtraSelectCount(@PathVariable(name = "page") int page, @PathVariable(name = "size") int size) {

        return classicModelsService.loadProductsWithoutExtraSelectCount(page, size);
    }
}

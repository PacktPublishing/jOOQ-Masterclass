package com.classicmodels.controller;

import com.classicmodels.service.ClassicModelsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassicModelsController {

    private final ClassicModelsService classicModelsService;

    public ClassicModelsController(ClassicModelsService classicModelsService) {
        this.classicModelsService = classicModelsService;
    }

    @GetMapping("/products/{limit}/{offset}")
    public String loadProductsPageAndMetadata(
            @PathVariable(name = "limit") int limit, @PathVariable(name = "offset") int offset) {

        return classicModelsService.loadProductsPageAndMetadata(limit, offset);
    }
}

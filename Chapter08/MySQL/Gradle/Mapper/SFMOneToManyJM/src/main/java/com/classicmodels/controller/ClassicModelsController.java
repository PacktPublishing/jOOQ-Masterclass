package com.classicmodels.controller;

import com.classicmodels.pojo.SimpleProductLine;
import com.classicmodels.service.ClassicModelsService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassicModelsController {

    private final ClassicModelsService productService;

    public ClassicModelsController(ClassicModelsService productService) {
        this.productService = productService;
    }

    @GetMapping("/productlines")
    public List<SimpleProductLine> fetchProductLineWithProducts() {

        return productService.fetchProductLineWithProducts();
    }
}

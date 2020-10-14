package com.classicmodels.controller;

import com.classicmodels.model.ProductLine;
import com.classicmodels.service.ClassicModelsService;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassicModelsController {

    private final ClassicModelsService classicModelsService;

    public ClassicModelsController(ClassicModelsService classicModelsService) {
        this.classicModelsService = classicModelsService;
    }

    @GetMapping("/productlineandproduct")
    public List<ProductLine> fetchProductLineAndProduct() {

        return classicModelsService.fetchProductLineAndProduct();
    }
    
    @GetMapping("/productlineonly")
    public List<ProductLine> fetchProductLineJooq() {

        return classicModelsService.fetchProductLineJooq();
    }
    
    @GetMapping("/updateproductlinejooq")
    public String updateProductLineDescriptionJooq() {

        classicModelsService.updateProductLineDescriptionJooq();
        
        return "Done via jOOQ!";
    }
}

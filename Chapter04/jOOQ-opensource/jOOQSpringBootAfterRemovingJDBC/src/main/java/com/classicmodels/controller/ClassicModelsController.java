package com.classicmodels.controller;

import com.classicmodels.model.ProductLine;
import com.classicmodels.service.ClassicModelsService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassicModelsController {

    private final ClassicModelsService classicModelsService;

    public ClassicModelsController(ClassicModelsService classicModelsService) {
        this.classicModelsService = classicModelsService;
    }

    @GetMapping("/productlineandproductjooq")
    public List<ProductLine> fetchProductLineAndProductJooq() {

        return classicModelsService.fetchProductLineAndProductJooq();
    }
    
    @GetMapping("/productlinejooq")
    public List<ProductLine> fetchProductLineJooq() {

        return classicModelsService.fetchProductLineJooq();
    }
    
    @GetMapping("/updateproductlinejooq")
    public String updateProductLineDescriptionJooq() {

        classicModelsService.updateProductLineDescriptionJooq();
        
        return "Done via jOOQ!";
    }
}

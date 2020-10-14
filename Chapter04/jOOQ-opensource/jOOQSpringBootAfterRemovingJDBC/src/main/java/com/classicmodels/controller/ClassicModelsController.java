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

    @GetMapping("/productlineandproduct")
    public List<ProductLine> fetchProductLineAndProduct() {

        return classicModelsService.fetchProductLineAndProduct();
    }
    
    @GetMapping("/productline")
    public List<ProductLine> fetchProductLine() {

        return classicModelsService.fetchProductLine();
    }
    
    @GetMapping("/updateproductline")
    public String updateProductLineDescription() {

        classicModelsService.updateProductLineDescription();
        
        return "Done via jOOQ!";
    }
}

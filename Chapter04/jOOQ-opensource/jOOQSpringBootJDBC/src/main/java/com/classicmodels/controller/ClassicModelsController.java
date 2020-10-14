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

    @GetMapping("/productlineandproductjdbc")
    public List<ProductLine> fetchProductLineAndProduct() {

        return StreamSupport.stream(
                classicModelsService.fetchProductLineAndProduct().spliterator(), false)
                .collect(Collectors.toList());
    }
    
    @GetMapping("/productlinejooq")
    public List<ProductLine> fetchProductLineJooq() {

        return classicModelsService.fetchProductLineJooq();
    }
    
    @GetMapping("/updateproductlinejdbc")
    public String updateProductLineDescription() {

        classicModelsService.updateProductLineDescription();
        
        return "Done via Spring Data JDBC!";
    }
    
    @GetMapping("/updateproductlinejooq")
    public String updateProductLineDescriptionJooq() {

        classicModelsService.updateProductLineDescriptionJooq();
        
        return "Done via jOOQ!";
    }
}

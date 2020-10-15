package com.classicmodels.controller;

import com.classicmodels.model.ProductLine;
import com.classicmodels.service.ClassicModelsService;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/updateproductlinejdbc")
    public String updateProductLineDescription(@RequestParam String id) {

        classicModelsService.updateProductLineDescription(id);

        return "Done via Spring Data JDBC!";
    }

    @GetMapping("/updateproductlinejooq")
    public String updateProductLineDescriptionJooq(@RequestParam String id) {

        classicModelsService.updateProductLineDescriptionJooq(id);

        return "Done via jOOQ!";
    }
}

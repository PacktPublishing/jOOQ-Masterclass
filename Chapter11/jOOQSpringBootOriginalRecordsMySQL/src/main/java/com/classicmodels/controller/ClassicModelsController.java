package com.classicmodels.controller;

import com.classicmodels.service.ClassicModelsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ClassicModelsController {

    protected static final String PRODUCT_ATTR = "product";

    private final ClassicModelsService classicModelsService;

    public ClassicModelsController(ClassicModelsService classicModelsService) {
        this.classicModelsService = classicModelsService;
    }

    @GetMapping("/product/{id}")
    public String loadProduct(@PathVariable(name = "id") Long id, Model model) {

        model.addAttribute(PRODUCT_ATTR, classicModelsService.loadProduct(id));

        return "product";
    }
}

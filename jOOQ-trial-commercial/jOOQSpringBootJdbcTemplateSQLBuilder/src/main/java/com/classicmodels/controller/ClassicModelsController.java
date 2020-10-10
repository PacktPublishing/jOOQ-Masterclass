package com.classicmodels.controller;

import com.classicmodels.pojo.Manager;
import com.classicmodels.service.ClassicModelsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassicModelsController {

    private final ClassicModelsService classicModelsService;

    public ClassicModelsController(ClassicModelsService classicModelsService) {
        this.classicModelsService = classicModelsService;
    }
    
    @GetMapping("/manager")
    public Manager fetchManager(@RequestParam Long managerId) {

        return classicModelsService.fetchManager(managerId);
    }                 
}
package com.classicmodels.controller;

import com.classicmodels.service.ClassicModelsService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassicModelsController {

    private final ClassicModelsService classicModelsService;

    public ClassicModelsController(ClassicModelsService classicModelsService) {
        this.classicModelsService = classicModelsService;
    }

    @GetMapping(value = "/offices", produces = MediaType.APPLICATION_JSON_VALUE)
    public String jsonOfficeDepartmentFluentReturn() {

        return classicModelsService.jsonOfficeDepartmentFluentReturn();
    }
}

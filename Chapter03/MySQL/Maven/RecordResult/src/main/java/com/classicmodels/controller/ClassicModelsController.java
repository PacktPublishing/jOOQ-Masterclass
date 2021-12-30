package com.classicmodels.controller;

import com.classicmodels.service.ClassicModelsService;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassicModelsController {

    private final ClassicModelsService classicModelsService;

    public ClassicModelsController(ClassicModelsService classicModelsService) {
        this.classicModelsService = classicModelsService;
    }

    @GetMapping("/customers")
    public List<Map<String, Object>> fetchCustomers() {

        return classicModelsService.fetchCustomers().intoMaps();
    }
}

package com.classicmodels.controller;

import com.classicmodels.pojo.SimpleBManager;
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

    @GetMapping("/managers.u")
    public List<SimpleBManager> fetchManyToManyUnidirectional() {

        return classicModelsService.fetchManyToManyUnidirectional();
    }
    
    @GetMapping("/managers.b")
    public List<SimpleBManager> fetchManyToManyBidirectional() {

        return classicModelsService.fetchManyToManyBidirectional();
    }
}

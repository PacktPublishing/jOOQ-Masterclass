package com.classicmodels.controller;

import com.classicmodels.service.ClassicModelsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClassicModelsController {
    
    private final ClassicModelsService classicModelsService;

    public ClassicModelsController(ClassicModelsService classicModelsService) {
        this.classicModelsService = classicModelsService;
    }        

    @GetMapping("/index")
    public String index() {
        
        return "index";
    }
    
    @GetMapping("/add")
    public String add() {
        
        classicModelsService.tenant();
        
        return "index";
    }
}

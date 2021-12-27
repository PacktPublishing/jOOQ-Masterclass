package com.classicmodels.controller;

import com.classicmodels.pojo.Office;
import com.classicmodels.service.ClassicModelsService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassicModelsController {

    private final ClassicModelsService classicModelsService;

    public ClassicModelsController(ClassicModelsService classicModelsService) {
        this.classicModelsService = classicModelsService;
    }

    @GetMapping("/officesInTerritory1")
    public List<Office> fetchOfficesInTerritory1(
            @RequestParam(name = "territory") String territory) {

        return classicModelsService.fetchOfficesInTerritory1(territory);
    }
    
    @GetMapping("/officesInTerritory2")
    public List<Office> fetchOfficesInTerritory2(
            @RequestParam(name = "territory") String territory) {

        return classicModelsService.fetchOfficesInTerritory2(territory);
    }
    
    @GetMapping("/officesInTerritory3")
    public List<Office> fetchOfficesInTerritory3(
            @RequestParam(name = "territory") String territory) {

        return classicModelsService.fetchOfficesInTerritory3(territory);
    }
    
    @GetMapping("/officesInTerritory4")
    public List<Office> fetchOfficesInTerritory4(
            @RequestParam(name = "territory") String territory) {

        return classicModelsService.fetchOfficesInTerritory4(territory);
    }
    
    @GetMapping("/officesInTerritory5")
    public String fetchOfficesInTerritory5(
            @RequestParam(name = "territory") String territory) {

        return classicModelsService.fetchOfficesInTerritory5(territory);
    }
    
    @GetMapping("/officesInTerritory6")
    public List<Office> fetchOfficesInTerritory6(
            @RequestParam(name = "territory") String territory) {

        return classicModelsService.fetchOfficesInTerritory6(territory);
    } 
}
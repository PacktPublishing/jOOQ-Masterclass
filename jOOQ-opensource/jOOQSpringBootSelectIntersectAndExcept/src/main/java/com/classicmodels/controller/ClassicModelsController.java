package com.classicmodels.controller;

import com.classicmodels.service.ClassicModelsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassicModelsController {

    private final ClassicModelsService classicModelsService;

    public ClassicModelsController(ClassicModelsService classicModelsService) {
        this.classicModelsService = classicModelsService;
    }

    @GetMapping("/citycountryIntersect")
    public Object[][] intersectOfficeCustomerCityAndCountry() {

        return classicModelsService.intersectOfficeCustomerCityAndCountry();
    }
    
    @GetMapping("/citycountryExcept")
    public Object[][] exceptOfficeCustomerCityAndCountry() {

        return classicModelsService.exceptOfficeCustomerCityAndCountry();
    }
}

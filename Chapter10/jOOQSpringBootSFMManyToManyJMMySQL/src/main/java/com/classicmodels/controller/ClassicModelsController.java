package com.classicmodels.controller;

import com.classicmodels.pojo.SimpleManager;
import com.classicmodels.service.ClasicModelsService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassicModelsController {

    private final ClasicModelsService clasicModelsService;

    public ClassicModelsController(ClasicModelsService clasicModelsService) {
        this.clasicModelsService = clasicModelsService;
    }

    @GetMapping("/manageroffice")
    public List<SimpleManager> fetchManagerAndOffice() {

        return clasicModelsService.fetchManagerAndOffice();
    }
}

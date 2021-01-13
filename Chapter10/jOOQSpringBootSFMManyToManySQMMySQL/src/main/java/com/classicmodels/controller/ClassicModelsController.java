package com.classicmodels.controller;

import com.classicmodels.pojo.SimpleManager;
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

    @GetMapping("/manageroffice")
    public List<SimpleManager> fetchManagerAndOffice() {

        return classicModelsService.fetchManagerAndOffice();
    }
}

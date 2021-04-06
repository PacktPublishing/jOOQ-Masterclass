package com.classicmodels.controller;

import com.classicmodels.service.ClassicModelsService;
import java.util.List;
import java.util.Map;
import jooq.generated.tables.pojos.Employee;
import jooq.generated.tables.pojos.Office;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassicModelsController {

    private final ClassicModelsService classicModelsService;

    public ClassicModelsController(ClassicModelsService classicModelsService) {
        this.classicModelsService = classicModelsService;
    }

    @GetMapping("/officeWithEmployees/{start}/{end}")
    public Map<Office, List<Employee>> loadOfficeWithEmploeePage(
            @PathVariable(name = "start") int start, @PathVariable(name = "end") int end) {

        return classicModelsService.loadOfficeWithEmploeePage(start, end);
    }
}

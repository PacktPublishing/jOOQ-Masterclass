package com.classicmodels.controller;

import com.classicmodels.service.ClassicModelsService;
import java.util.List;
import jooq.generated.tables.pojos.Sale;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassicModelsController {

    private final ClassicModelsService classicModelsService;

    public ClassicModelsController(ClassicModelsService classicModelsService) {
        this.classicModelsService = classicModelsService;
    }

    /* call jOOQ user-defined DAOs */
    
    @GetMapping("/saleAscGtLimit")
    public List<Sale> fetchSaleAscGtLimit(
            @RequestParam(name = "limit") double limit) {

        return classicModelsService.fetchSaleAscGtLimit(limit);
    }

    @GetMapping("/saleByFiscalYear")
    public List<Sale> fetchSaleByFiscalYear(
            @RequestParam(name = "year") int year) {

        return classicModelsService.fetchSaleByFiscalYear(year);
    }
}
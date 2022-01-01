package com.classicmodels.controller;

import com.classicmodels.service.ClassicModelsService;
import java.util.List;
import jooq.generated.tables.pojos.Sale;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassicModelsController {

    private final ClassicModelsService salesManagementService;

    public ClassicModelsController(ClassicModelsService salesManagementService) {
        this.salesManagementService = salesManagementService;
    }

    /* call jOOQ user-defined DAOs */
    @GetMapping("/saleAscGtLimit")
    public List<Sale> fetchSaleAscGtLimit(
            @RequestParam(name = "limit") double limit) {

        return salesManagementService.fetchSaleAscGtLimit(limit);
    }

    /* call jOOQ generated DAOs */
    @GetMapping("/saleByFiscalYear")
    public List<Sale> fetchSaleByFiscalYear(
            @RequestParam(name = "year") int year) {

        return salesManagementService.fetchSaleByFiscalYear(year);
    }
}

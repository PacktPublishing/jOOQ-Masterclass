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
    @GetMapping("/saleByFiscalYear")
    public List<Sale> fetchSaleByFiscalYear(
            @RequestParam(name = "year") int year) {

        return classicModelsService.fetchSaleByFiscalYear(year);
    }

    @GetMapping("/saleAscGtLimit")
    public List<Sale> fetchSaleAscGtLimit(
            @RequestParam(name = "limit") double limit) {

        return classicModelsService.fetchSaleAscGtLimit(limit);
    }

    /* call Spring Data JPA DAOs */
    @GetMapping("/top10By")
    public List<com.classicmodels.entity.Sale> fetchTop10By() {

        return classicModelsService.fetchTop10By();
    }

    /* call jOOQ user-defined generic DAOs */
    @GetMapping("/allSales")
    public List<Sale> fetchAllSales() {

        return classicModelsService.fetchAllSales();
    }
    
    @GetMapping("/deleteSaleById")
    public void deleteSaleById(
            @RequestParam(name = "id") Integer id) {

        classicModelsService.deleteSaleById(id);
    }
}

package com.classicmodels.controller;

import com.classicmodels.service.SalesManagementService;
import java.util.List;
import jooq.generated.tables.pojos.Sale;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SalesManagementController {

    private final SalesManagementService salesManagementService;

    public SalesManagementController(SalesManagementService salesManagementService) {
        this.salesManagementService = salesManagementService;
    }

    /* call jOOQ user-defined DAOs */
    @GetMapping("/saleByFiscalYear")
    public List<Sale> fetchSaleByFiscalYear(@RequestParam int year) {

        return salesManagementService.fetchSaleByFiscalYear(year);
    }

    @GetMapping("/saleAscGtLimit")
    public List<Sale> fetchSaleAscGtLimit(@RequestParam int limit) {

        return salesManagementService.fetchSaleAscGtLimit(limit);
    }

    /* call Spring Data JPA DAOs */
    @GetMapping("/top10By")
    public List<com.classicmodels.entity.Sale> fetchTop10By() {

        return salesManagementService.fetchTop10By();
    }

    /* call jOOQ user-defined generic DAOs */
    @GetMapping("/allSales")
    public List<Sale> fetchAllSales() {

        return salesManagementService.fetchAllSales();
    }
}

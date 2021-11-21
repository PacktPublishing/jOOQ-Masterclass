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

    /* call methods from user-defined jOOQ DAO */
    @GetMapping("/saleByFiscalYear")
    public List<Sale> fetchSaleByFiscalYear(@RequestParam int year) {

        return salesManagementService.fetchSaleByFiscalYear(year);
    }

    @GetMapping("/saleAscGtLimit")
    public List<Sale> fetchSaleAscGtLimit(@RequestParam int limit) {

        return salesManagementService.fetchSaleAscGtLimit(limit);
    }

    /* call methods from Spring Data JPA DAO */
    @GetMapping("/top10By")
    public List<com.classicmodels.entity.Sale> fetchTop10By() {

        return salesManagementService.fetchTop10By();
    }

    @GetMapping("/all")
    public List<com.classicmodels.entity.Sale> fetchAll() {

        return salesManagementService.fetchAll();
    }

    /* call jOOQ generated DAO */
    @GetMapping("/bySaleId")
    public List<Sale> fetchBySaleId(@RequestParam Long... ids) {

        return salesManagementService.fetchBySaleId(ids);
    }

    @GetMapping("/rangeOfSale")
    public List<Sale> fetchRangeOfSale(@RequestParam Double lb, @RequestParam Double ub) {

        return salesManagementService.fetchRangeOfSale(lb, ub);
    }
}

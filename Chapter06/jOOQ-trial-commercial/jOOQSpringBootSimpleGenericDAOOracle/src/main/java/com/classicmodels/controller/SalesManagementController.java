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

    /* call jOOQ user-defined DAO */
    @GetMapping("/saleAscGtLimit")
    public List<Sale> fetchSaleAscGtLimit(@RequestParam int limit) {

        return salesManagementService.fetchSaleAscGtLimit(limit);
    }

    @GetMapping("/saleByFiscalYear")
    public List<Sale> fetchSaleByFiscalYear(@RequestParam int year) {

        return salesManagementService.fetchSaleByFiscalYear(year);
    }    
    
    /* call jOOQ user-defined generic DAO */
    @GetMapping("/allSale")
    List<Sale> fetchAllSale() {

        return salesManagementService.fetchAllSale();
    }
    
    @GetMapping("/deleteSaleById")
    public void deleteSaleById(@RequestParam Long id) {

        salesManagementService.deleteSaleById(id);
    }
}

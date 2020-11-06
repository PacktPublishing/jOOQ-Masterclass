package com.classicmodels.controller;

import com.classicmodels.service.SalesManagementService;
import java.time.LocalDate;
import java.util.List;
import jooq.generated.tables.pojos.Order;
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

    @GetMapping("/saleAscGtLimit")
    public List<Sale> fetchSaleAscGtLimit(@RequestParam int limit) {

        return salesManagementService.fetchSaleAscGtLimit(limit);
    }

    @GetMapping("/saleByFiscalYear")
    public List<Sale> fetchSaleByFiscalYear(@RequestParam int year) {

        return salesManagementService.fetchSaleByFiscalYear(year);
    }

    @GetMapping("/orderDescByDate")
    public List<Order> fetchOrderDescByDate() {

        return salesManagementService.fetchOrderDescByDate();
    }

    @GetMapping("/orderBetweenDate")
    public List<Order> fetchOrderBetweenDate(@RequestParam String sd, @RequestParam String ed) {

        return salesManagementService.fetchOrderBetweenDate(
                LocalDate.parse(sd), LocalDate.parse(ed));
    }
}

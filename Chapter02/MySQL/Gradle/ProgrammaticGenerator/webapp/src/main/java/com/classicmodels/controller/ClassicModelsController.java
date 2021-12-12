package com.classicmodels.controller;

import com.classicmodels.pojo.CustomerAndOrder;
import com.classicmodels.service.ClassicModelsService;
import java.time.LocalDate;
import java.util.List;
import jooq.generated.tables.pojos.JooqOffice;
import jooq.generated.tables.pojos.JooqOrder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassicModelsController {

    private final ClassicModelsService classicModelsService;

    public ClassicModelsController(ClassicModelsService classicModelsService) {
        this.classicModelsService = classicModelsService;
    }

    @GetMapping("/officesInTerritory")
    public List<JooqOffice> fetchOfficesInTerritory(
	      @RequestParam(name = "territory") String territory) {

        return classicModelsService.fetchOfficesInTerritory(territory);
    }

    @GetMapping("/ordersByRequiredDate")
    public List<JooqOrder> fetchOrdersByRequiredDate(
            @RequestParam(name = "startDate") String startDate, 
            @RequestParam(name = "endDate") String endDate) {

        return classicModelsService.fetchOrdersByRequiredDate(
                LocalDate.parse(startDate), LocalDate.parse(endDate));
    }

    @GetMapping("/customersAndOrders")
    public List<CustomerAndOrder> fetchCustomersAndOrders() {

        return classicModelsService.fetchCustomersAndOrders();
    }

}
package com.classicmodels.controller;

import com.classicmodels.service.ClassicModelsService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassicModelsController {

    private final ClassicModelsService classicModelsService;

    public ClassicModelsController(ClassicModelsService classicModelsService) {
        this.classicModelsService = classicModelsService;
    }
/*
    @GetMapping("/sumsales")
    public List<EmployeePojo> fetchEmployeesBySumSales() {

        return classicModelsService.fetchEmployeesBySumSales();
    }

    @GetMapping("/maxbuyprice")
    public List<ProductPojo> fetchProductsMaxBuyPriceByProductionLine() {

        return classicModelsService.fetchProductsMaxBuyPriceByProductionLine();
    }

    @GetMapping("/customers")
    public List<CustomerPojo> fetchCustomerFullNameCityCountry() {

        return classicModelsService.fetchCustomerFullNameCityCountry();
    }       
*/
}

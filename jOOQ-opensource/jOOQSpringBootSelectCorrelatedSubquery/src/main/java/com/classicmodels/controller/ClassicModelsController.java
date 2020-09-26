package com.classicmodels.controller;

import com.classicmodels.pojo.CustomerPojo;
import com.classicmodels.pojo.EmployeePojo;
import com.classicmodels.pojo.ProductPojo;
import com.classicmodels.service.ClassicModelsService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassicModelsController {

    private final ClassicModelsService classicModelsService;

    public ClassicModelsController(ClassicModelsService classicModelsService) {
        this.classicModelsService = classicModelsService;
    }

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
}

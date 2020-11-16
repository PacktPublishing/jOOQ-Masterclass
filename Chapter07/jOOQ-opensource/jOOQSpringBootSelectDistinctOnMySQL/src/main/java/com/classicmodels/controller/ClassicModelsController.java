package com.classicmodels.controller;

import com.classicmodels.service.ClassicModelsService;
import java.util.List;
import jooq.generated.tables.pojos.Product;
import jooq.generated.tables.pojos.Sale;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassicModelsController {

    private final ClassicModelsService classicModelsService;

    public ClassicModelsController(ClassicModelsService classicModelsService) {
        this.classicModelsService = classicModelsService;
    }

    @GetMapping("/products")
    public List<Product> fetchProductsByVendorScale() {

        return classicModelsService.fetchProductsByVendorScale();
    }
    
    @GetMapping("/sales")
    public List<Sale> fetchEmployeeNumberOfMaxSalePerFiscalYear() {

        return classicModelsService.fetchEmployeeNumberOfMaxSalePerFiscalYear();
    }
}

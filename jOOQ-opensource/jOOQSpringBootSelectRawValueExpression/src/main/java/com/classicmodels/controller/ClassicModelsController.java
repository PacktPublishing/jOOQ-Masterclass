package com.classicmodels.controller;

import com.classicmodels.service.ClassicModelsService;
import java.util.List;
import jooq.generated.tables.pojos.Customerdetail;
import jooq.generated.tables.pojos.Order;
import jooq.generated.tables.pojos.Product;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassicModelsController {

    private final ClassicModelsService classicModelsService;

    public ClassicModelsController(ClassicModelsService classicModelsService) {
        this.classicModelsService = classicModelsService;
    }
    
    
    @GetMapping("/cp1")
    // e.g., http://localhost:8080/cp1?vendor=Carousel%20DieCast%20Legends&scale=1:24&productLine=Classic%20Cars
    public List<Product> fetchProductsByVendorScaleAndProductLine(
            @RequestParam String vendor, @RequestParam String scale, @RequestParam String productLine) {
     
        return classicModelsService.fetchProductsByVendorScaleAndProductLine(vendor, scale, productLine);
    }
    
    @GetMapping("/cp2")
    // e.g., http://localhost:8080/cp2?productId=63
    public Product fetchProductByIdVendorAsCarouselDieCastLegendsScaleAndProductLine(
            @RequestParam Long productId) {
     
        return classicModelsService.fetchProductByIdVendorAsCarouselDieCastLegendsScaleAndProductLine(productId);
    }

    @GetMapping("/ip1")
    // e.g., http://localhost:8080/ip1
    public List<Product> fetchProductsByVendorScaleAndProductLineIn() {
        
        return classicModelsService.fetchProductsByVendorScaleAndProductLineIn();
    }
          
    @GetMapping("/ip2")
    // e.g., http://localhost:8080/ip2
    public List<Product> fetchProductsByVendorScaleAndProductLineInCollection() {
        
        return classicModelsService.fetchProductsByVendorScaleAndProductLineInCollection();
    }
    
    @GetMapping("/ip3")
    // e.g., http://localhost:8080/ip3
    public List<Customerdetail> fetchProductsByVendorScaleAndProductLineInSelect() {
        
        return classicModelsService.fetchProductsByVendorScaleAndProductLineInSelect();
    }
    
    @GetMapping("/bp3")
    // e.g., http://localhost:8080/bp3
    public List<Order> fetchOrdersBetweenOrderDateAndShippedDate() {
        
        return classicModelsService.fetchOrdersBetweenOrderDateAndShippedDate();
    }
}

package com.classicmodels.controller;

import com.classicmodels.service.ClassicModelsService;
import java.util.List;
import jooq.generated.tables.pojos.Employee;
import jooq.generated.tables.pojos.Orderdetail;
import jooq.generated.tables.pojos.Product;
import jooq.generated.tables.pojos.Productline;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassicModelsController {

    private final ClassicModelsService classicModelsService;

    public ClassicModelsController(ClassicModelsService classicModelsService) {
        this.classicModelsService = classicModelsService;
    }

    @GetMapping("/products/asc/{id}/{size}")
    public List<Product> loadProductsAsc(@PathVariable(name = "id") int productId,
            @PathVariable(name = "size") int size) {

        return classicModelsService.loadProductsAsc(productId, size);
    }

    @GetMapping("/products/desc/{id}/{size}")
    public List<Product> loadProductsDesc(@PathVariable(name = "id") int productId,
            @PathVariable(name = "size") int size) {

        return classicModelsService.loadProductsDesc(productId, size);
    }

    @GetMapping("/employees1/{officeCode}/{salary}/{size}")
    public List<Employee> loadEmployeesOfficeCodeAscSalaryDesc(@PathVariable(name = "officeCode") String officeCode,
            @PathVariable(name = "salary") int salary, @PathVariable(name = "size") int size) {

        return classicModelsService.loadEmployeesOfficeCodeAscSalaryDesc(officeCode, salary, size);
    }

    @GetMapping("/employees2/{officeCode}/{salary}/{size}")
    public List<Employee> loadEmployeesOfficeCodeAscSalaryAsc(@PathVariable(name = "officeCode") String officeCode,
            @PathVariable(name = "salary") int salary, @PathVariable(name = "size") int size) {

        return classicModelsService.loadEmployeesOfficeCodeAscSalaryAsc(officeCode, salary, size);
    }

    @GetMapping("/orderdetail/{orderId}/{productId}/{quantityOrdered}/{size}")
    public List<Orderdetail> loadOrderdetailPageOrderIdAscProductIdQuantityOrderedDesc(
            @PathVariable(name = "orderId") long orderId, @PathVariable(name = "productId") long productId,
            @PathVariable(name = "quantityOrdered") int quantityOrdered, @PathVariable(name = "size") int size) {

        return classicModelsService.loadOrderdetailPageOrderIdAscProductIdQuantityOrderedDesc(
                orderId, productId, quantityOrdered, size);
    }

    @GetMapping("/products/{productId}/{size}")
    public List<Product> loadProductsBuyPriceGtMsrp(
            @PathVariable(name = "productId") long productId, @PathVariable(name = "size") int size) {

        return classicModelsService.loadProductsBuyPriceGtMsrp(productId, size);
    }
    
    @GetMapping("/productline/{productLine}/{code}/{size}")
    public List<Productline> loadProductlineEmbeddedKey(
            @PathVariable(name = "productLine") String productLine, 
            @PathVariable(name = "code") long code,
            @PathVariable(name = "size") int size) {

        return classicModelsService.loadProductlineEmbeddedKey(productLine, code, size);
    }
    
    @GetMapping("/orderdetail/{orderId}/{size}")
    public String loadOrderdetailPageGroupBy(
            @PathVariable(name = "orderId") long orderId, @PathVariable(name = "size") int size) {

        return classicModelsService.loadOrderdetailPageGroupBy(orderId, size);
    }
}

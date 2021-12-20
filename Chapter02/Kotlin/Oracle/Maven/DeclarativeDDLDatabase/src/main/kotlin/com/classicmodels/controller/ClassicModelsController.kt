package com.classicmodels.controller

import com.classicmodels.service.ClassicModelsService
import java.time.LocalDate
import com.classicmodels.pojo.Office
import com.classicmodels.pojo.Order
import com.classicmodels.pojo.CustomerAndOrder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
public class ClassicModelsController(private val classicModelsService: ClassicModelsService) {

    @GetMapping("/officesInTerritory")
    fun fetchOfficesInTerritory(
            @RequestParam(name = "territory") territory: String): MutableList<Office> {        
        return classicModelsService.fetchOfficesInTerritory(territory)
    }

    @GetMapping("/ordersByRequiredDate")
    fun fetchOrdersByRequiredDate(
            @RequestParam(name = "startDate") startDate: String,
            @RequestParam(name = "endDate") endDate: String): MutableList<Order> {        
        return classicModelsService.fetchOrdersByRequiredDate(
                LocalDate.parse(startDate), LocalDate.parse(endDate))
    }

    @GetMapping("/customersAndOrders")
    fun fetchCustomersAndOrders(): MutableList<CustomerAndOrder> {
        return classicModelsService.fetchCustomersAndOrders()
    }
}

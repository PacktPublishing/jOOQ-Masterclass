package com.classicmodels

import com.classicmodels.service.ClassicModelsService
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class Initializer (private val classicModelsService: ClassicModelsService) : ApplicationRunner {

    @Throws(Exception::class)
    override fun run(args: ApplicationArguments) {
        println("Fetching offices from 'NA' territory:")
        println(classicModelsService.fetchOfficesInTerritory("NA"))

        println("Fetching orders between 2002-01-01 and 2004-12-31:")
        println(classicModelsService
             .fetchOrdersByRequiredDate(LocalDate.of(2002, 1, 1), LocalDate.of(2004, 12, 31)))

        println("Fetching customers and orders:")
        println(classicModelsService.fetchCustomersAndOrders())
    }
}
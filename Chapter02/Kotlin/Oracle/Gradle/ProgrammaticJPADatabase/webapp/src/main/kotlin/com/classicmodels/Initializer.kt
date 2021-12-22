package com.classicmodels

import com.classicmodels.service.ClassicModelsService
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class Initializer (private val classicModelsService: ClassicModelsService) : ApplicationRunner {

    @Throws(Exception::class)
    override fun run(args: ApplicationArguments) {

        // uses jOOQ
        println("Fetch employees and least salary:")
        println(classicModelsService.fetchEmployeesAndLeastSalary())

        // uses Spring Data JPA
        println("Fetch the employees  by job title:")
        println(classicModelsService.fetchByJobTitle("Sales Rep"))
    }
}
package com.classicmodels

import com.classicmodels.service.ClassicModelsService
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class Initializer (private val classicModelsService: ClassicModelsService) : ApplicationRunner {

    @Throws(Exception::class)
    override fun run(args: ApplicationArguments) {
        println("Fetching offices from 'NA' territory:")
        println(classicModelsService.fetchOfficesInTerritory("NA"))
    }
}
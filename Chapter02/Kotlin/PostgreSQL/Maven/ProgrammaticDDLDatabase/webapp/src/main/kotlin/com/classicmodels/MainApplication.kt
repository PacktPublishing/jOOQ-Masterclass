package com.classicmodels

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
 
@SpringBootApplication(exclude = [R2dbcAutoConfiguration::class])
@ComponentScan(basePackages = ["jooq.generated.tables.daos", "com.classicmodels"])
class MainApplication

fun main(args: Array<String>) {   
    runApplication<MainApplication>(*args)
}

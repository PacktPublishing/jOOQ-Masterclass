package com.classicmodels.service

import com.classicmodels.repository.ClassicModelsRepository
import jooq.generated.tables.pojos.JooqOffice
import jooq.generated.tables.pojos.JooqOrder
import com.classicmodels.pojo.CustomerAndOrder
import java.time.LocalDate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
 
@Service
@Transactional(readOnly = true)
class ClassicModelsService(private val classicModelsRepository: ClassicModelsRepository) {

    fun fetchOfficesInTerritory(territory: String): MutableList<JooqOffice> {
        return classicModelsRepository.findOfficesInTerritory(territory)
    }

    fun fetchOrdersByRequiredDate(startDate: LocalDate, endDate: LocalDate): MutableList<JooqOrder> {
        return classicModelsRepository.findOrdersByRequiredDate(startDate, endDate)
    }

    fun fetchCustomersAndOrders(): MutableList<CustomerAndOrder> {
        return classicModelsRepository.findCustomersAndOrders()
    }
}
package com.classicmodels.service

import com.classicmodels.repository.ClassicModelsRepository
import jooq.generated.tables.pojos.JooqOffice
import jooq.generated.tables.pojos.JooqOrder
import com.classicmodels.pojo.CustomerAndOrder
import java.time.LocalDate
import kotlin.collections.List
import org.springframework.stereotype.Service
import jooq.generated.tables.daos.OfficeRepository
import jooq.generated.tables.daos.OrderRepository
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ClassicModelsService(private val classicModelsRepository: ClassicModelsRepository,
                           private val officeRepository: OfficeRepository,
                           private val orderRepository: OrderRepository) {

    fun fetchOfficesInTerritory(territory: String): List<JooqOffice> {
        return officeRepository.fetchByTerritory(territory)
    }
    
    fun fetchOrdersByRequiredDate(startDate: LocalDate, endDate: LocalDate): List<JooqOrder> {
        return orderRepository.fetchRangeOfRequiredDate(startDate, endDate)
    }

    fun fetchCustomersAndOrders(): MutableList<CustomerAndOrder> {
        return classicModelsRepository.findCustomersAndOrders()
    }
}
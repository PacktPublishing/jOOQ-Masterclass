package com.classicmodels.service

import com.classicmodels.repository.ClassicModelsRepository
import com.classicmodels.pojo.Office
import com.classicmodels.pojo.Order
import com.classicmodels.pojo.CustomerAndOrder
import java.time.LocalDate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ClassicModelsService(private val classicModelsRepository: ClassicModelsRepository) {

    fun fetchOfficesInTerritory(territory: String): MutableList<Office> {
        return classicModelsRepository.findOfficesInTerritory(territory)
    }

    fun fetchOrdersByRequiredDate(startDate: LocalDate, endDate: LocalDate): MutableList<Order> {
        return classicModelsRepository.findOrdersByRequiredDate(startDate, endDate)
    }

    fun fetchCustomersAndOrders(): MutableList<CustomerAndOrder> {
        return classicModelsRepository.findCustomersAndOrders()
    }
}
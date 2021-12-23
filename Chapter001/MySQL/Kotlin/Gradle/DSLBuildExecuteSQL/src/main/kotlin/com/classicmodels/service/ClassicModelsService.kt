package com.classicmodels.service

import org.springframework.stereotype.Service
import com.classicmodels.repository.ClassicModelsRepository
import com.classicmodels.pojo.Office

@Service
class ClassicModelsService(private val classicModelsRepository: ClassicModelsRepository) {

    fun fetchOfficesInTerritory(territory: String): MutableList<Office> {
        return classicModelsRepository.findOfficesInTerritory(territory)
    }
}
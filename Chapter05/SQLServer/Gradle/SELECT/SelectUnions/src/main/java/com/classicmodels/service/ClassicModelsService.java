package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import org.springframework.stereotype.Service;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    public void callAll() {
        classicModelsRepository.unionEmployeeAndCustomerNames();                                 // EXAMPLE 1
        classicModelsRepository.unionEmployeeAndCustomerNamesConcatColumns();                    // EXAMPLE 2   
        classicModelsRepository.unionEmployeeAndCustomerNamesDifferentiate();                    // EXAMPLE 3
        classicModelsRepository.unionEmployeeAndCustomerNamesOrderBy();                          // EXAMPLE 4        
        classicModelsRepository.unionAllOfficeCustomerCityAndCountry();                          // EXAMPLE 5        
        classicModelsRepository.findAllOrdersHavingQuantityOrderedLe20AndGe60();                 // EXAMPLE 6
        classicModelsRepository.findSilverGoldPlatinumCustomers();                               // EXAMPLE 7
    }
}
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
        classicModelsRepository.unionEmployeeAndCustomerNames();                                  // EXAMPLE 1
        classicModelsRepository.unionEmployeeAndCustomerNamesConcatColumns();                     // EXAMPLE 2   
        classicModelsRepository.unionEmployeeAndCustomerNamesDifferentiate();                     // EXAMPLE 3
        classicModelsRepository.unionEmployeeAndCustomerNamesOrderBy();                           // EXAMPLE 4
        classicModelsRepository.unionEmployeeSmallestAndHighestSalary();                          // EXAMPLE 5
        classicModelsRepository.unionAllOfficeCustomerCityAndCountry();                           // EXAMPLE 6
        classicModelsRepository.findMinMaxWorstBestPrice();                                       // EXAMPLE 7
        classicModelsRepository.findTop5OrdersHavingQuantityOrderedLe20AndGe60OrderedByPrice();   // EXAMPLE 8
        classicModelsRepository.findProductStockLt500Gt9500();                                    // EXAMPLE 9        
        classicModelsRepository.findSilverGoldPlatinumCustomers();                                // EXAMLPE 10
    }
}
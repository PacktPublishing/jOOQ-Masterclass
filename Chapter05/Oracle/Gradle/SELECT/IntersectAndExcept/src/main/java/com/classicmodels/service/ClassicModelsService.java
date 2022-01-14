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

        classicModelsRepository.intersectBuyPriceWithPriceEach();               // EXAMPLE 1
        classicModelsRepository.exceptBuyPriceFromPriceEach();                  // EXAMPLE 2
        classicModelsRepository.intersectOfficeCustomerCityAndCountry();        // EXAMPLE 3 
        classicModelsRepository.exceptOfficeCustomerCityAndCountry();           // EXAMPLE 4
        classicModelsRepository.findCitiesWithNoOffices();                      // EXAMPLE 5

        classicModelsRepository.emulateIntersectOfficeCustomerCityAndCountry(); // EXAMPLE 6
        classicModelsRepository.emulateExceptOfficeCustomerCityAndCountry();    // EXAMPLE 7       
    }

}

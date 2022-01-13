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

        classicModelsRepository.findDistinctOfficesCityCountry();                            // EXAMPLE 1
        classicModelsRepository.findOfficeDistinctFromAddress();                             // EXAMPLE 2
        classicModelsRepository.findDistinctAndNotDistinctPaymentDates();                    // EXAMPLE 3
        classicModelsRepository.findOfficeAndCustomerOfficePostalCodeDistinctCityCountry();  // EXAMPLE 4
        classicModelsRepository.countPaymentCachingDate();                                   // EXAMPLE 5
        classicModelsRepository.findProductLineHavingMaxNrOfProducts();                      // EXAMPLE 6 
        classicModelsRepository.avgSumMinMaxPriceEach();                                     // EXAMPLE 7        
        classicModelsRepository.countDistinctSalesByEmployeeNumber();                        // EXAMPLE 8
        classicModelsRepository.findProductsByVendorScale();                                 // EXAMPLE 9    
        classicModelsRepository.findEmployeeNumberOfMaxSalePerFiscalYear();                  // EXAMPLE 10                                        
        classicModelsRepository.findDistinctEmployeeNumberOrderByMinSale();                  // EXAMPLE 11
    }
}

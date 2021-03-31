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
        
        classicModelsRepository.fetchEmployeeNameOfficeCityInnerJoin();                       // EXAMPLE 1
        classicModelsRepository.fetchEmployeeNameSaleLeftOuterJoin();                         // EXAMPLE 2
        classicModelsRepository.fetchEmployeeNameSaleLeftOuterJoinExclusive();                // EXAMPLE 3
        classicModelsRepository.fetchEmployeeNameSaleRightOuterJoin();                        // EXAMPLE 4
        classicModelsRepository.fetchEmployeeNameSaleRightOuterJoinExclusive();               // EXAMPLE 5
        classicModelsRepository.fetchOfficeCustomerdetailFullOuterJoin();                     // EXAMPLE 6
        classicModelsRepository.fetchOfficeCustomerdetailFullOuterJoinExclusive();            // EXAMPLE 7
        classicModelsRepository.fetchOfficeCustomerdetailFullOuterJoinViaUnion();             // EXAMPLE 8
        classicModelsRepository.fetchOfficeCustomerdetailFullOuterJoinExclusiveViaUnion();    // EXAMPLE 9                
        classicModelsRepository.fetchEmployeeSaleByYear();                                    // EXAMPLE 10
        classicModelsRepository.crossJoinFirst2EmployeeFirst2Office();                        // EXAMPLE 11
        classicModelsRepository.innerJoinFirst5EmployeeFirst5Office();                        // EXAMPLE 12
        classicModelsRepository.insertOfficesInEachCountryOfCustomer();                       // EXAMPLE 13        
    }
}
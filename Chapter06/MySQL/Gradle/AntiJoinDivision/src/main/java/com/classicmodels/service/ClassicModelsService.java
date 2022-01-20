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

        classicModelsRepository.fetchOrderContainingTop3ProductsViaInnerJoin();                 // EXAMPLE 1
        classicModelsRepository.fetchOrderContainingTop3ProductsViaWhereNotExists();            // EXAMPLE 2
        classicModelsRepository.fetchOrderContainingTop3ProductViaAntiJoin();                   // EXAMPLE 3 
        classicModelsRepository.fetchOrderContainingTop3ProductViajOOQ();                       // EXAMPLE 4        
        classicModelsRepository.fetchOrderContainingAtLeastCertainProductsViaInnerJoin();       // EXAMPLE 5
        classicModelsRepository.fetchOrderContainingAtLeastCertainProductsViaWhereNotExists();  // EXAMPLE 6
        classicModelsRepository.fetchOrderContainingAtLeastCertainProductsViaAntiJoin();        // EXAMPLE 7
        classicModelsRepository.fetchOrderContainingAtLeastCertainProductsViajOOQ();            // EXAMPLE 8
    }
}

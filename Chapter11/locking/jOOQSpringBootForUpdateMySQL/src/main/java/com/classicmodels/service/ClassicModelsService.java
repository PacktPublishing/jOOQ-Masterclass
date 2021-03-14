package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import org.springframework.stereotype.Service;

// This app ends with an exception: 
        // MySQLTransactionRollbackException: Lock wait timeout exceeded; try restarting transaction

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    public void fetchProductsViaTwoTransactions() throws InterruptedException {

        classicModelsRepository.fetchProductsViaTwoTransactions();
    }
}

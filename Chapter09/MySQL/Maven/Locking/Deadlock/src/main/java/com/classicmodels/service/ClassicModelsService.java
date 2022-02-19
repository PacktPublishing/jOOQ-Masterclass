package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import org.springframework.stereotype.Service;

// This application produces an exception of type:
// com.mysql.cj.jdbc.exceptions.MySQLTransactionRollbackException: 
// Deadlock found when trying to get lock; try restarting transaction
    
// However, the database will retry until one of the transaction (A) succeeds

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

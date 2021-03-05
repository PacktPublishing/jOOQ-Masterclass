package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import jooq.generated.tables.pojos.BankTransaction;
import jooq.generated.tables.records.BankTransactionRecord;
import org.jooq.Result;
import org.springframework.stereotype.Service;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    public Result<BankTransactionRecord> loadAllBankTransactionOfCertainPayment() {

        return classicModelsRepository.fetchAllBankTransactionOfCertainPayment();
    }
    
    public int insertOrUpdateBankTransaction(String bt) {
        
       return classicModelsRepository.insertOrUpdateBankTransaction(bt);
    }

    public BankTransactionRecord loadBankTransaction(Long id) {

        return classicModelsRepository.fetchBankTransaction(id);
    }

    public void newBankTransaction(BankTransaction ntb) {

        classicModelsRepository.newBankTransaction(ntb);
    }

    public void updateBankTransaction(BankTransaction utb) {

        classicModelsRepository.updateBankTransaction(utb);
    }

    public void deleteBankTransaction(BankTransaction dtb) {

        classicModelsRepository.deleteBankTransaction(dtb);
    }
}

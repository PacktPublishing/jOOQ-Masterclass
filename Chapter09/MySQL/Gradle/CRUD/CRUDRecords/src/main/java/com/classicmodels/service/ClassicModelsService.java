package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
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

    public BankTransactionRecord loadBankTransaction(Long id) {

        return classicModelsRepository.fetchBankTransaction(id);
    }

    public void newBankTransaction(BankTransactionRecord btr) {

        // we should know to which payment this transaction belongs
        btr.setCustomerNumber(333L);
        btr.setCheckNumber("NF959653");

        int inserted = classicModelsRepository.newBankTransaction(btr);
        
        // take decision based on *inserted* value        
    }

    public void updateBankTransaction(BankTransactionRecord btr) {

        int updated = classicModelsRepository.updateBankTransaction(btr);
        
        // take decision based on *updated* value        
    }

    public void deleteBankTransaction(BankTransactionRecord btr) {

        int deleted = classicModelsRepository.deleteBankTransaction(btr);
        
        // take decision based on *deleted* value        
    }
}
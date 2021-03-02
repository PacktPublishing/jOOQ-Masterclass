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

    public Result<BankTransactionRecord> fetchAllBankTransactionOfCertainPayment() {

        return classicModelsRepository.fetchAllBankTransactionOfCertainPayment();
    }

    public BankTransactionRecord fetchBankTransaction(Long id) {

        return classicModelsRepository.fetchBankTransaction(id);
    }

    public int newBankTransaction(BankTransactionRecord ntb) {

        return classicModelsRepository.newBankTransaction(ntb);
    }

    public int updateBankTransaction(BankTransactionRecord utb) {

        return classicModelsRepository.updateBankTransaction(utb);
    }

    public int deleteBankTransaction(BankTransactionRecord dtb) {

        return classicModelsRepository.deleteBankTransaction(dtb);
    }
}

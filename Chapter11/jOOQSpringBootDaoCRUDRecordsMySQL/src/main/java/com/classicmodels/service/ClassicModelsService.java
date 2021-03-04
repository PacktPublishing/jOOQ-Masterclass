package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import java.util.List;
import jooq.generated.tables.pojos.BankTransaction;
import org.springframework.stereotype.Service;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    public List<BankTransaction> loadAllBankTransactionOfCertainPayment() {

        return classicModelsRepository.fetchAllBankTransactionOfCertainPayment();
    }

    public BankTransaction loadBankTransaction(Long id) {

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

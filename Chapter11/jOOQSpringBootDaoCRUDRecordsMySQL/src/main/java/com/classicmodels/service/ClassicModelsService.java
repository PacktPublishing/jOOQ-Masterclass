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

    public void newBankTransaction(BankTransaction bt) {

        // we should know to which payment this transaction belongs
        bt.setCustomerNumber(333L);
        bt.setCheckNumber("NF959653");
        
        classicModelsRepository.newBankTransaction(bt);
    }

    public void updateBankTransaction(BankTransaction bt) {

        classicModelsRepository.updateBankTransaction(bt);
    }

    public void deleteBankTransaction(BankTransaction bt) {

        classicModelsRepository.deleteBankTransaction(bt);
    }
}

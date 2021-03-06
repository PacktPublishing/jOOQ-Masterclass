package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import jooq.generated.tables.records.PaymentRecord;
import org.jooq.Result;
import org.springframework.stereotype.Service;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    public Result<PaymentRecord> loadAllPayment103() {

        return classicModelsRepository.fetchAllPayment103();
    }
    
    public PaymentRecord loadPayment(Long nr, String ch) {

        return classicModelsRepository.fetchPayment(nr, ch);
    }

    public int storePayment(PaymentRecord pr) {

        return classicModelsRepository.storePayment(pr);
    }
    
    public void refreshPayment(PaymentRecord pr) {
        
        classicModelsRepository.refreshPayment(pr);
    }
}

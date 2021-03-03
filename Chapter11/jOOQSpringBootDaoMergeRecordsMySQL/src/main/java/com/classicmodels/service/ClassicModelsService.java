package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import java.util.List;
import jooq.generated.tables.pojos.Payment;
import jooq.generated.tables.records.PaymentRecord;
import org.springframework.stereotype.Service;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    public List<Payment> loadAllPayment103() {

        return classicModelsRepository.fetchAllPayment103();
    }

    public void mergePayment(Payment p) {

        classicModelsRepository.mergePayment(p);
    }
}

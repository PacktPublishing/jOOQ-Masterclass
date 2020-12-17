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

        classicModelsRepository.joinPaymentBankTransactionViaOn();                    // EXAMPLE 1
        classicModelsRepository.joinPaymentBankTransactionViaOnRow();                 // EXAMPLE 2
        classicModelsRepository.joinPaymentBankTransactionViaOnKey();                 // EXAMPLE 3
        classicModelsRepository.joinProductlineProductlinedetailViaOnKeyTF();         // EXAMPLE 4
        classicModelsRepository.joinProductlineProductlinedetailViaOnKeyFK();         // EXAMPLE 5                
        classicModelsRepository.joinProductlineProductlinedetailViaOnKeyTF1();        // EXAMPLE 6
        classicModelsRepository.joinProductlineProductlinedetailViaOnKey();           // EXAMPLE 7
    }
}

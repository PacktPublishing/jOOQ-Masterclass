package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import java.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    @Transactional(readOnly = true)
    public void callAll() {
        classicModelsRepository.printPaymentAndCachingDateCast();
        classicModelsRepository.printPaymentAndCachingDateCoerce();
        
        classicModelsRepository.coerceResultQueryToAnotherResultQuery();

        classicModelsRepository.printProductPriceAndDescCoerce();
        classicModelsRepository.printProductPriceAndDescCast();

        classicModelsRepository.printInvoicesPerDayCoerce(LocalDate.of(2003, 4, 9));
        classicModelsRepository.printInvoicesPerDayCast(LocalDate.of(2003, 4, 9));

        classicModelsRepository.printProductsName();                
    }

}

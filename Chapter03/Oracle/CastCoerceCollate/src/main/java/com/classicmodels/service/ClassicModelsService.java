package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import java.time.LocalDate;
import org.springframework.stereotype.Service;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }
    
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

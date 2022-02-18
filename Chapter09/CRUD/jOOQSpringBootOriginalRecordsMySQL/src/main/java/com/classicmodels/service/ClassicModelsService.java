package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import jooq.generated.tables.records.ProductRecord;
import org.springframework.stereotype.Service;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    public ProductRecord loadProduct(Long id) {

        var product = classicModelsRepository.fetchProduct(id);

        if (product != null) {

            // run some clever algorithm that makes price 
            // and msrp predictions based on current product data
            product.setBuyPrice(BigDecimal.valueOf(Math.random() * 99.99).setScale(2, RoundingMode.HALF_UP));
            product.setMsrp(BigDecimal.valueOf(Math.random() * 99.99).setScale(2, RoundingMode.HALF_UP));

            return product;
        }

        return new ProductRecord();
    }
}

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

        classicModelsRepository.saleHarmonicMean();
        classicModelsRepository.saleGeometricMean();
        classicModelsRepository.sdSale();
        classicModelsRepository.productBuyPriceVariance();
        classicModelsRepository.covarianceProductBuyPriceMSRP();
        classicModelsRepository.regressionProductBuyPriceMSRP();
        classicModelsRepository.linearRegression();
        classicModelsRepository.boolAndOrSample();
        classicModelsRepository.bitsOperationsSample();
        classicModelsRepository.medianSample();
        classicModelsRepository.cmgrSale();        
        classicModelsRepository.modeSales();
        classicModelsRepository.approxCountDistinct();
    }
}

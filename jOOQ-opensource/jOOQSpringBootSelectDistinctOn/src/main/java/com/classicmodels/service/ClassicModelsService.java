package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import java.util.List;
import jooq.generated.tables.pojos.Product;
import jooq.generated.tables.pojos.Sale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    @Transactional(readOnly = true)
    public List<Product> fetchProductsByVendorScale() {

        return classicModelsRepository.findProductsByVendorScale();
    }

    @Transactional(readOnly = true)
    public List<Sale> fetchEmployeeNumberOfMaxSalePerFiscalYear() {
        
        return classicModelsRepository.findEmployeeNumberOfMaxSalePerFiscalYear();
    }
}

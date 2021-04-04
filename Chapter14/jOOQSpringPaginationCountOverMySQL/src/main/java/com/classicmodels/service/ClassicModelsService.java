package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import jooq.generated.tables.pojos.Product;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    public Page<Product> loadProducts(int page, int size) {

        return classicModelsRepository.fetchProductsPage(page, size);
    }
}

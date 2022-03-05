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

    public Page<Product> loadProductsWithoutExtraSelectCount(int page, int size) {

        return classicModelsRepository.fetchProductsPageWithoutExtraSelectCount(page, size);
    }

    public Page<Product> loadProductsExtraSelectCount(int page, int size) {

        return classicModelsRepository.fetchProductsPageExtraSelectCount(page, size);
    }
}

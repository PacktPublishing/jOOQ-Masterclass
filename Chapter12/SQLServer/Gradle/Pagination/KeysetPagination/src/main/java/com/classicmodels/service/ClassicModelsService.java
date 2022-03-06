package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import java.util.List;
import jooq.generated.tables.pojos.Product;
import org.springframework.stereotype.Service;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    public List<Product> loadProducts(long productId, int size) {

        return classicModelsRepository.fetchProductsPage(productId, size);
    }
}

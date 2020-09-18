package com.classicmodels.service;

import com.classicmodels.model.ProductLine;
import com.classicmodels.repository.ClassicModelsRepository;
import org.springframework.stereotype.Service;
import com.classicmodels.repository.ProductlineRepository;
import java.util.List;

@Service
public class ProductLineService {

    private final ProductlineRepository productLineRepository;
    private final ClassicModelsRepository classicModelsRepository;

    public ProductLineService(ProductlineRepository productLineRepository,
            ClassicModelsRepository classicModelsRepository) {
        this.productLineRepository = productLineRepository;
        this.classicModelsRepository = classicModelsRepository;
    }

    public Iterable<ProductLine> fetchProductLineAndProduct() {
        // Spring Data JDBC always fetches the entire aggregate via N+1 queries
        return productLineRepository.findAll();
    }

    public List<ProductLine> fetchOnlyProductLine() {
        // jOOQ fetches only the data from 'productline'
        return classicModelsRepository.fetchOnlyProductLine();
    }
}

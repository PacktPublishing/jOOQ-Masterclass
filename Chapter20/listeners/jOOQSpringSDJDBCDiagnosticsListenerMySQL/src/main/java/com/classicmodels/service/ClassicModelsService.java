package com.classicmodels.service;

import com.classicmodels.model.ProductLine;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.classicmodels.repository.ProducLineRepository;

@Service
public class ClassicModelsService {

    private final ProducLineRepository productLineRepository;

    public ClassicModelsService(ProducLineRepository productLineRepository) {
        this.productLineRepository = productLineRepository;        
    }

    public Iterable<ProductLine> fetchProductLineAndProduct() {
        // Spring Data JDBC always fetches the entire aggregate via N+1 queries
        return productLineRepository.findAll();
    }
    
    @Transactional
    public void updateProductLineDescription(String id) {
    
        // Spring Data JDBC removes all products, updates the product line and insert the products back
        ProductLine pl = productLineRepository.findById(id).get();
        pl.setTextDescription("Lorem ipsum dolor sit amet via JDBC");
        
        productLineRepository.save(pl);
    }
        
    public void updateProductLineDescriptionJooq(String id) {
        
        // jOOQ uses a single update query to achieve the same result
        productLineRepository.updateProductLineDescriptionJooq(id);
    }
}

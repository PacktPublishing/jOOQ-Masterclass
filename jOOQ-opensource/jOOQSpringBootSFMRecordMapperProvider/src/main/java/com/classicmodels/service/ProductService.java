package com.classicmodels.service;

import com.classicmodels.pojo.ProductDTO;
import com.classicmodels.repository.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> fetchProductsNameVendorAndStock() {

        return productRepository.findProductsNameVendorAndStock();
    }

}

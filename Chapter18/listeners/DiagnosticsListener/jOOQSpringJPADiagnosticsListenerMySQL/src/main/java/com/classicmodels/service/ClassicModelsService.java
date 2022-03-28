package com.classicmodels.service;

import com.classicmodels.entity.Product;
import com.classicmodels.entity.Productline;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.classicmodels.repository.ProductlineRepository;
import com.classicmodels.repository.ProductRepository;

@Service
public class ClassicModelsService {

    private final ProductlineRepository productlineRepository;
    private final ProductRepository productRepository;

    public ClassicModelsService(ProductlineRepository productlineRepository,
            ProductRepository productRepository) {

        this.productRepository = productRepository;
        this.productlineRepository = productlineRepository;
    }

    @Transactional(readOnly = true)
    public void fetchProductsAndProductlines() {

        List<Product> products = productRepository.findAll();

        for (Product product : products) {

            Productline productline = product.getProductLine();

            System.out.println("Product: " + product.getProductName()
                    + " Product Line: " + productline.getProductLine());
        }
    }

    @Transactional(readOnly = true)
    public void fetchProductlinesAndProducts() {

        List<Productline> productlines = productlineRepository.findAll();

        for (Productline productline : productlines) {

            List<Product> products = productline.getProducts();

            System.out.println("Productline: " + productline.getProductLine()
                    + " Products: " + products);
        }
    }
}
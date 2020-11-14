package com.classicmodels.service;

import com.classicmodels.repository.ClassicModelsRepository;
import java.util.List;
import jooq.generated.tables.pojos.Customerdetail;
import jooq.generated.tables.pojos.Order;
import jooq.generated.tables.pojos.Product;
import org.jooq.Row3;
import static org.jooq.impl.DSL.row;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClassicModelsService {

    private final ClassicModelsRepository classicModelsRepository;

    public ClassicModelsService(ClassicModelsRepository classicModelsRepository) {
        this.classicModelsRepository = classicModelsRepository;
    }

    @Transactional(readOnly = true)
    public List<Product> fetchProductsByVendorScaleAndProductLine(
            String vendor, String scale, String productLine) {

        return classicModelsRepository.findProductsByVendorScaleAndProductLine(vendor, scale, productLine);
    }

    @Transactional(readOnly = true)
    public Product fetchProductByIdVendorAsCarouselDieCastLegendsScaleAndProductLine(Long productId) {

        return classicModelsRepository.findProductByIdVendorAsCarouselDieCastLegendsScaleAndProductLine(productId);
    }

    @Transactional(readOnly = true)
    public List<Product> fetchProductsByVendorScaleAndProductLineIn() {

        return classicModelsRepository.findProductsByVendorScaleAndProductLineIn();
    }

    @Transactional(readOnly = true)
    public List<Product> fetchProductsByVendorScaleAndProductLineInCollection() {

        List<Row3<String, String, String>> rows = List.of(
                row("Carousel DieCast Legends", "1:24", "Classic Cars"),
                row("Exoto Designs", "1:18", "Vintage Cars")
        );
        
        return classicModelsRepository.findProductsByVendorScaleAndProductLineInCollection(rows);
    }
    
    @Transactional(readOnly = true)
    public List<Customerdetail> fetchProductsByVendorScaleAndProductLineInSelect() {
        
        return classicModelsRepository.findProductsByVendorScaleAndProductLineInSelect();
    }
    
    @Transactional(readOnly = true)
    public List<Order> fetchOrdersBetweenOrderDateAndShippedDate() {
        
        return classicModelsRepository.findOrdersBetweenOrderDateAndShippedDate();
    }
}

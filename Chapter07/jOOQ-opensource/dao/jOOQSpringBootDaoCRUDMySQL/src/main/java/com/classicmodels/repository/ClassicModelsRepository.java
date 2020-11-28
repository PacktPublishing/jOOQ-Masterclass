package com.classicmodels.repository;

import com.classicmodels.pojo.ProductPart;
import java.math.BigDecimal;
import jooq.generated.tables.daos.ProductRepository;
import jooq.generated.tables.pojos.JooqProduct;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final ProductRepository productRepository;
    
    private final JooqProduct jooqProduct = new JooqProduct();         // jOOQ generated POJO
    private final ProductPart productPart = new ProductPart();         // user-defined POJO
        
    public ClassicModelsRepository(ProductRepository productRepository) {        
        this.productRepository = productRepository;
    }

    @Transactional
    public void insertProductViajOOQDao() {
        
        // insert jOOQ POJO        
        jooqProduct.setProductName("Giant Motor XP");
        productRepository.insert(jooqProduct);                    
        
        // insert user-defined POJO
        productPart.setProductName("2010 797B");
        productPart.setProductScale("1:20");
        productPart.setProductConsumption("30 kWh/100 mi"); // transient field
        productPart.setProductStatus("available");          // transient field
        productRepository.insert(productPart);
    }
    
    @Transactional
    public void updateProductViajOOQDao() {
     
        // update jOOQ POJO        
        jooqProduct.setProductVendor("USA Labs B1");
        productRepository.update(jooqProduct);
        
        // update user-defined POJO
        productPart.setBuyPrice(BigDecimal.valueOf(243.22));
        productRepository.update(productPart);        
    }
    
    @Transactional
    public void mergeProductViajOOQDao() {
        
        // merge jOOQ POJO        
        jooqProduct.setProductVendor("USA Laboratory B1");
        productRepository.merge(jooqProduct);
        
        // update user-defined POJO
        productRepository.merge(productPart);
    }
    
    @Transactional
    public void deleteProductViajOOQDao() {
        
        // delete jOOQ POJO
        productRepository.delete(jooqProduct);
        
        // delete user-defined POJO
        productRepository.delete(productPart);
    }
}
package com.classicmodels.repository;

import com.classicmodels.pojo.ExtraPoduct;
import java.math.BigDecimal;
import jooq.generated.tables.daos.ProductRepository;
import jooq.generated.tables.daos.TokenRepository;
import jooq.generated.tables.pojos.Product;
import jooq.generated.tables.pojos.Token;
import org.jooq.DSLContext;
import org.jooq.conf.Settings;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;
    private final ProductRepository productRepository;
    private final TokenRepository tokenRepository;

    private final Product jooqProduct = new Product();                 // jOOQ generated POJO
    private final ExtraPoduct extraProduct = new ExtraPoduct();        // user-defined POJO

    public ClassicModelsRepository(DSLContext ctx, 
            ProductRepository productRepository, TokenRepository tokenRepository) {
        this.ctx = ctx;
        this.productRepository = productRepository;
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public void insertProductViajOOQDao() {

        // insert jOOQ POJO        
        jooqProduct.setProductName("Giant Motor XP");
        jooqProduct.setCode(599302L);
        productRepository.insert(jooqProduct);

        // insert user-defined POJO
        extraProduct.setProductName("2010 797B");
        extraProduct.setProductScale("1:20");
        extraProduct.setProductConsumption("30 kWh/100 mi"); // transient field
        extraProduct.setProductStatus("available");          // transient field
        extraProduct.setCode(599302L);
        productRepository.insert(extraProduct);
    }

    @Transactional
    public void updateProductViajOOQDao() {

        // update jOOQ POJO        
        jooqProduct.setProductVendor("USA Labs B1");
        productRepository.update(jooqProduct);

        // update user-defined POJO
        extraProduct.setBuyPrice(BigDecimal.valueOf(243.22));
        productRepository.update(extraProduct);
    }

    @Transactional
    public void mergeProductViajOOQDao() {

        // merge jOOQ POJO        
        jooqProduct.setProductVendor("USA Laboratory B1");
        productRepository.merge(jooqProduct);

        // merge user-defined POJO
        productRepository.merge(extraProduct);
    }

    @Transactional
    public void deleteProductViajOOQDao() {

        // delete jOOQ POJO
        productRepository.delete(jooqProduct);

        // delete user-defined POJO
        productRepository.delete(extraProduct);
    }

    @Transactional
    public void recordToPojo() {
        ctx.configuration().set(new Settings()
                .withReturnRecordToPojo(true)           // default (setting this to false suppress the effect of withReturnAllOnUpdatableRecord()
                .withReturnAllOnUpdatableRecord(true)); // setting this to true returns all fields not just the primary key

        Token token = new Token();
        token.setAmount(50.5);
        token.setSaleId(1L);
        tokenRepository.insert(token);

        System.out.println("Token: " + token);
    }
}

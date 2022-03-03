package com.classicmodels.repository;

import static jooq.generated.tables.Product.PRODUCT;
import jooq.generated.tables.records.ProductRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }
    
    @Transactional
    public void returnIdentitiesOnInsertInto() {
        
        var result = ctx.insertInto(PRODUCT)
                .set(PRODUCT.PRODUCT_LINE, "Vintage Cars")
                .set(PRODUCT.CODE, 223113L)
                .set(PRODUCT.PRODUCT_NAME, "Rolls-Royce Dawn Drophead")
                .returningResult(PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_UID)
                .fetch();
        
        System.out.println("The inserted record ID: " + result.get(0).value1());               
        System.out.println("The inserted record 'product_uid' IDENTITY: " + result.get(0).value2());
    }

    @Transactional
    public void returnIdentitiesOnUpdatableRecord() {

        ProductRecord pr = ctx.newRecord(PRODUCT);

        pr.setProductLine("Classic Cars");
        pr.setCode(599302L);
        pr.setProductName("1967 Chevrolet Camaro RS");

        pr.insert();

        System.out.println("The inserted record ID: " + pr.getProductId());
        
        // the *pr.getProductUid()* works thanks to the synthetic identity configured in pom.xml
        System.out.println("The inserted record 'product_uid' IDENTITY: " + pr.getProductUid());
    }

}

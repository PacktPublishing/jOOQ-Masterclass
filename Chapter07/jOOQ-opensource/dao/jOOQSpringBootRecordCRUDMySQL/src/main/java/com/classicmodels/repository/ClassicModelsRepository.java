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
    private ProductRecord pr = new ProductRecord();

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;

        // attaching the record to the current configuration
        pr.attach(ctx.configuration());
    }

    @Transactional
    public void insertProductViajOOQDao() {

        System.out.println("EXAMPLE 1.1 (rows affected): "
                + pr.insert()
        );

        pr.setProductName("Kia 88 Plane");
        pr.setProductScale("1:10");
        pr.setProductDescription("This is an amazing plane");
        pr.setProductLine("Planes");
        System.out.println("EXAMPLE 1.2 (rows affected): "
                + pr.insert(PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_SCALE)
        );
    }

    @Transactional
    public void updateProductViajOOQDao() {

        System.out.println("EXAMPLE 2.1 (rows affected): "
                + pr.update()
        );
        
        pr.setProductScale("1:18");
        System.out.println("EXAMPLE 2.2 (rows affected): "
                + pr.update(PRODUCT.PRODUCT_SCALE)
        );
    }
    
    @Transactional
    public void deleteProductViajOOQDao() {
        
        System.out.println("EXAMPLE 3 (rows affected): "
                + pr.delete()
        );
        
        pr.detach();
    }
}

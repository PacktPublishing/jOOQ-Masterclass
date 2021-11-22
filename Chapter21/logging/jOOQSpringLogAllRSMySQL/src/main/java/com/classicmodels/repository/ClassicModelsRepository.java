package com.classicmodels.repository;

import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Product.PRODUCT;
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

    public void selectProductsAndLog() {

        // var result =
                ctx.select(ORDERDETAIL.ORDER_ID, ORDERDETAIL.QUANTITY_ORDERED, 
                           PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME)
                        .from(ORDERDETAIL)
                        .join(PRODUCT)
                        .on(ORDERDETAIL.PRODUCT_ID.eq(PRODUCT.PRODUCT_ID))                      
                        .fetch();

        // System.out.println("Result:\n" + result.format(result.size()));                 
    }
}

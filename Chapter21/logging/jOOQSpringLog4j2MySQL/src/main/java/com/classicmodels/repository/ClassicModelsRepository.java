package com.classicmodels.repository;

import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Productline.PRODUCTLINE;
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
        
        ctx.select(PRODUCTLINE.PRODUCT_LINE, PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR)
                .from(PRODUCTLINE)
                .join(PRODUCT)
                .on(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                .where(PRODUCT.QUANTITY_IN_STOCK.gt(5000))
                .fetch();
    }
}

package com.classicmodels.repository;

import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Productline.PRODUCTLINE;
import org.jooq.DSLContext;
import org.jooq.conf.Settings;
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

        // this query uses the default jOOQ logging
        ctx.select(PRODUCTLINE.PRODUCT_LINE, PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR)
                .from(PRODUCTLINE)
                .join(PRODUCT)
                .on(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                .fetch();
    }

    public void selectProductsButDontLog() {

        // this query will not be logged since we have turned off logging
        ctx.configuration().derive(
                new Settings()                        
                        .withExecuteLogging(Boolean.FALSE))
                .dsl()
                .select(PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR)
                .from(PRODUCT)
                .fetch();
    }
}

package com.classicmodels.repository;

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

    @Transactional
    public void tenant() {

        ctx.insertInto(PRODUCT, PRODUCT.PRODUCT_NAME, PRODUCT.QUANTITY_IN_STOCK)
                .values("Product", 100)
                .execute();
    }
}

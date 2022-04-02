package com.classicmodels.repository;

import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.param;
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

        ctx.select(PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME)
                .from(PRODUCT)
                .where(PRODUCT.QUANTITY_IN_STOCK.gt(5000)
                        .and(PRODUCT.CODE.in(
                                param("vintageCars", 223113L),
                                param("trucksAndBuses", 569331L),
                                param("planes", 433823L))))
                .fetch();

    }
}

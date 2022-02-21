package com.classicmodels.repository;

import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void sample() {

        ctx.transaction(outer -> {
            outer.dsl()
                    .insertInto(PRODUCT, PRODUCT.PRODUCT_ID, PRODUCT.CODE, 
                            PRODUCT.PRODUCT_NAME, PRODUCT.QUANTITY_IN_STOCK)
                    .values(865L, 599302L,"Product_86", 100)
                    .onDuplicateKeyIgnore()
                    .execute();

            DSL.using(outer).transaction(inner -> {
                inner.dsl()
                        .insertInto(PRODUCT, PRODUCT.PRODUCT_ID, PRODUCT.CODE, 
                                PRODUCT.PRODUCT_NAME, PRODUCT.QUANTITY_IN_STOCK)
                        .values(987L, 599302L, "Product_1", 100)
                        .onDuplicateKeyIgnore()
                        .execute();
            });
        });
    }
}

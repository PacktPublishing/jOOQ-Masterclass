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

    public ProductRecord fetchProduct(Long id) {

        var product = ctx.selectFrom(PRODUCT)
                .where(PRODUCT.PRODUCT_ID.eq(id))
                .fetchSingle();

        return product;
    }
}

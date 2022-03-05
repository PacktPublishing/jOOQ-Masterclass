package com.classicmodels.repository;

import java.util.List;
import jooq.generated.tables.pojos.Product;
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

    public List<Product> fetchProductsPage(long productId, int size) {

        List<Product> result = ctx.selectFrom(PRODUCT)
                .where(PRODUCT.PRODUCT_ID.lt(productId))
                .orderBy(PRODUCT.PRODUCT_ID.desc())
                .limit(size)
                .fetchInto(Product.class);

        return result;
    }

}

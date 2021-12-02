package com.classicmodels.repository;

import java.util.stream.Collectors;
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

        ctx.select(PRODUCT.PRODUCT_LINE, PRODUCT.PRODUCT_ID,
                PRODUCT.PRODUCT_NAME, PRODUCT.BUY_PRICE)
                .from(PRODUCT)
                .limit(20)
                .fetch();

        ctx.select(ORDERDETAIL.ORDER_ID, ORDERDETAIL.PRODUCT_ID,
                ORDERDETAIL.PRICE_EACH, PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_LINE,
                PRODUCT.PRODUCT_NAME, PRODUCT.BUY_PRICE)
                .from(ORDERDETAIL)
                .join(PRODUCT)
                .on(ORDERDETAIL.PRODUCT_ID.eq(PRODUCT.PRODUCT_ID))
                .fetch();

        ctx.selectFrom(PRODUCT)
                .where(PRODUCT.PRODUCT_LINE.eq("Classic Cars"))
                .orderBy(PRODUCT.BUY_PRICE)
                .limit(5)
                .fetch();

        // the chart is not available for this on
        ctx.selectFrom(PRODUCT)
                .collect(Collectors.toList());
    }
}

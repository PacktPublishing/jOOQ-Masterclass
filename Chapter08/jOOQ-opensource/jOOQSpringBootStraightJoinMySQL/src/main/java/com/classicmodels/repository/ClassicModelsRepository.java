package com.classicmodels.repository;

import static jooq.generated.tables.Order.ORDER;
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

    // EXAMPLE 1
    public void straightJoinForceJoinOrder() {

        System.out.println("EXAMPLE 1\n"
                + ctx.select(PRODUCT.PRODUCT_ID, ORDER.ORDER_ID)
                        .from(PRODUCT)
                        .straightJoin(ORDERDETAIL).on(ORDERDETAIL.PRODUCT_ID.eq(PRODUCT.PRODUCT_ID))
                        .innerJoin(ORDER).on(ORDER.ORDER_ID.eq(ORDERDETAIL.ORDER_ID))
                        .fetch()
        );
    }

}

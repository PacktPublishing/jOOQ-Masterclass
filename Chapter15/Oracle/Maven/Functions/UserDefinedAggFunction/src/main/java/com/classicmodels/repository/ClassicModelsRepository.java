package com.classicmodels.repository;

import java.math.BigDecimal;
import static jooq.generated.Routines.secondMax;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
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

    public void callSecondMax() {

        ctx.select(secondMax(ORDERDETAIL.QUANTITY_ORDERED), ORDERDETAIL.PRODUCT_ID)
                .from(ORDERDETAIL)
                .groupBy(ORDERDETAIL.PRODUCT_ID)
                .having(secondMax(ORDERDETAIL.QUANTITY_ORDERED).gt(BigDecimal.valueOf(55)))
                .fetch();
    }

}

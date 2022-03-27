package com.classicmodels.repository;

import static jooq.generated.db1.Db1.DB1;
import static jooq.generated.db2.Db2.DB2;
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

    public void tenant() {

        ctx.select().from(DB1.PRODUCTLINE).fetch();
        ctx.select().from(DB2.PRODUCT).fetch();

        // join DB1 and DB2
        ctx.select(DB1.PRODUCTLINE.PRODUCT_LINE,
                DB2.PRODUCT.PRODUCT_ID, DB2.PRODUCT.PRODUCT_NAME, DB2.PRODUCT.QUANTITY_IN_STOCK)
                .from(DB1.PRODUCTLINE)
                .join(DB2.PRODUCT)
                .on(DB1.PRODUCTLINE.PRODUCT_LINE.eq(DB2.PRODUCT.PRODUCT_LINE))
                .fetch();
    }
}

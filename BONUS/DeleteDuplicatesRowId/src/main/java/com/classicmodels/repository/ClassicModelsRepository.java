package com.classicmodels.repository;

import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.min;
import static org.jooq.impl.DSL.select;
import static org.jooq.util.oracle.OracleDSL.rowid;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    // Inspired by: https://blogs.oracle.com/sql/post/how-to-find-and-delete-duplicate-rows-with-sql       
    public void delete() {

        ctx.deleteFrom(ORDERDETAIL)
                .where(rowid().notIn(select(min(rowid()))
                        .from(ORDERDETAIL)
                        .groupBy(ORDERDETAIL.PRODUCT_ID, ORDERDETAIL.QUANTITY_ORDERED)))
                .execute();
    }
}

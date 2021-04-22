package com.classicmodels.repository;

import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.cast;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.cumeDist;
import static org.jooq.impl.DSL.round;
import org.jooq.impl.SQLDataType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    /*  The CUME_DIST() is a window function that returns the cumulative distribution of a value 
        within a set of values. It represents the number of rows with values less than or equal 
        to that row’s value divided by the total number of rows. The returned value of the CUME_DIST() 
        function is greater than zero and less than or equal one (0 < CUME_DIST() <= 1). 
        The repeated column values receive the same CUME_DIST() value. */
    
    public void cumeDistProductBuyPrice() {

        ctx.select(PRODUCT.PRODUCT_NAME, PRODUCT.QUANTITY_IN_STOCK,
                round(cast(cumeDist().over().orderBy(PRODUCT.QUANTITY_IN_STOCK), SQLDataType.NUMERIC), 2).as("cume_dist"))
                .from(PRODUCT)
                .fetch();

        // emulate CUME_DIST() 
        ctx.select(PRODUCT.PRODUCT_NAME, PRODUCT.QUANTITY_IN_STOCK,
                round(((cast(count().over().orderBy(PRODUCT.QUANTITY_IN_STOCK).rangeUnboundedPreceding(), Double.class)).divide(
                        count().over().orderBy(PRODUCT.QUANTITY_IN_STOCK).rangeBetweenUnboundedPreceding()
                                .andUnboundedFollowing())), 2).as("cume_dist"))
                .from(PRODUCT)
                .fetch();
    }
}

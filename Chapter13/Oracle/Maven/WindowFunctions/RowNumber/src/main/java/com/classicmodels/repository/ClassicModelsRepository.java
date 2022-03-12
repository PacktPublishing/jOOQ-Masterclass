package com.classicmodels.repository;

import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Table;
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.min;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.rowNumber;
import static org.jooq.impl.DSL.select;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    /*  The ROW_NUMBER() is a window function that assigns a sequential 
        number to each row (it starts from 1) */
    
    // simple use case, just assign a sequential number
    public void dummyAssignSequentialNumberToProducts() {

        ctx.select(rowNumber().over().orderBy(PRODUCT.PRODUCT_NAME).as("seq_nr"),
                PRODUCT.PRODUCT_NAME, PRODUCT.BUY_PRICE)
                .from(PRODUCT)
                .orderBy(PRODUCT.PRODUCT_NAME)
                .fetch();
    }

    public void medianProductQuantityInStock() {

        Field<Integer> x = PRODUCT.QUANTITY_IN_STOCK.as("x");
        Field<Double> y = inline(2.0d).mul(rowNumber().over().orderBy(PRODUCT.QUANTITY_IN_STOCK))
                .minus(count().over()).as("y");

        ctx.select(avg(x).as("median"))
                .from(select(x, y)
                        .from(PRODUCT))
                .where(y.between(0d, 2d))
                .fetch();
    }

    public void islandsOrder() {

        Table<?> t = select(ORDER.REQUIRED_DATE.as("rdate"), ORDER.STATUS.as("status"),
                (rowNumber().over().orderBy(ORDER.REQUIRED_DATE)
                        .minus(rowNumber().over().partitionBy(ORDER.STATUS)
                                .orderBy(ORDER.REQUIRED_DATE))).as("cluster_nr"))
                .from(ORDER).asTable("t");

        System.out.println(ctx.select(min(t.field("rdate")).as("cluster_start"),
                max(t.field("rdate")).as("cluster_end"),
                min(t.field("status")).as("cluster_score"))
                .from(t)
                .groupBy(t.field("cluster_nr"))
                .orderBy(1)
                .fetch().format(100));
    }

    public void returnEvery10thProduct() {
        
        ctx.select(field(name("t", "pn")))
                .from(select(PRODUCT.PRODUCT_NAME,
                        rowNumber().over().orderBy(PRODUCT.PRODUCT_NAME)).from(PRODUCT)
                        .asTable("t", "pn", "seq"))
                .where(field(name("t", "seq")).mod(10).eq(0))
                .fetch();
        
        // or, less verbose
        ctx.select(field("product_name"))
                .from(select(PRODUCT.PRODUCT_NAME,
                        rowNumber().over().orderBy(PRODUCT.PRODUCT_NAME).as("seq")).from(PRODUCT))
                .where(field(name("seq")).mod(10).eq(0))
                .fetch();
        
        // or, via QUALIFY
        ctx.select(PRODUCT.PRODUCT_NAME)
                .from(PRODUCT)
                .qualify(rowNumber().over().orderBy(PRODUCT.PRODUCT_NAME).mod(10).eq(0))
                .fetch();
    }
}

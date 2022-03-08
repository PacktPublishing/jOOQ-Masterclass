package com.classicmodels.repository;

import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.firstValue;
import static org.jooq.impl.DSL.lastValue;
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

    /*  - FIRST_VALUE() returns the value of the specified expression with 
          respect to the first row in the window frame.
    
        - NTH_VALUE() returns value of argument from Nth row of the window frame (not supported by SQL Server).
    
        - LAST_VALUE() returns the value of the specified expression with 
          respect to the last row in the window frame. */
    
    public void cheapestAndMostExpensiveProduct() {

        ctx.select(PRODUCT.PRODUCT_NAME, PRODUCT.BUY_PRICE,
                firstValue(PRODUCT.PRODUCT_NAME).over().orderBy(PRODUCT.BUY_PRICE).as("cheapest"),
                lastValue(PRODUCT.PRODUCT_NAME).over().orderBy(PRODUCT.BUY_PRICE)
                        .rangeBetweenUnboundedPreceding().andUnboundedFollowing().as("most_expensive"))
                .from(PRODUCT)
                .fetch();
    }

    public void cheapestAndMostExpensiveProductByProductLine() {

        ctx.select(PRODUCT.PRODUCT_LINE, PRODUCT.PRODUCT_NAME, PRODUCT.BUY_PRICE,
                firstValue(PRODUCT.PRODUCT_NAME).over()
                        .partitionBy(PRODUCT.PRODUCT_LINE).orderBy(PRODUCT.BUY_PRICE).as("cheapest"),
                lastValue(PRODUCT.PRODUCT_NAME).over()
                        .partitionBy(PRODUCT.PRODUCT_LINE).orderBy(PRODUCT.BUY_PRICE)
                        .rangeBetweenUnboundedPreceding().andUnboundedFollowing().as("most_expensive"))
                .from(PRODUCT)
                .fetch();
    }

    // SQL Server doesn't support NTH_VALUE(), but we can emulate it via ROW_NUMBER()
    // ------------------------------------------------------------------------------
    public void secondCheapestProduct() {

        ctx.select().from(
                select(PRODUCT.PRODUCT_NAME, PRODUCT.BUY_PRICE,
                        rowNumber()
                                .over().orderBy(PRODUCT.BUY_PRICE).as("second_cheapest"))
                        .from(PRODUCT))
                .where(field(name("second_cheapest")).eq(2))
                .fetch();
        
        // using QUALIFY
        var secondCheapest = rowNumber().over().orderBy(PRODUCT.BUY_PRICE);
        ctx.select(PRODUCT.PRODUCT_NAME, PRODUCT.BUY_PRICE, secondCheapest.as("second_cheapest"))
                        .from(PRODUCT)
                .qualify(secondCheapest.eq(2))
                .fetch();
    }

    public void secondMostExpensiveProductByProductLine() {

        // SQL Server doesn't support fromLast() so we order desc        
        ctx.select().from(
                select(PRODUCT.PRODUCT_LINE, PRODUCT.PRODUCT_NAME, PRODUCT.BUY_PRICE,
                        rowNumber().over()
                                .partitionBy(PRODUCT.PRODUCT_LINE)
                                .orderBy(PRODUCT.BUY_PRICE.desc()).as("second_most_expensive"))
                        .from(PRODUCT))
                .where(field(name("second_most_expensive")).eq(2))
                .fetch();
        
        // using QUALIFY
        var secondMostExpensive = rowNumber().over().partitionBy(PRODUCT.PRODUCT_LINE)
                                .orderBy(PRODUCT.BUY_PRICE.desc());
        ctx.select(PRODUCT.PRODUCT_LINE, PRODUCT.PRODUCT_NAME, PRODUCT.BUY_PRICE,
                        secondMostExpensive.as("second_most_expensive"))
                        .from(PRODUCT)
                .qualify(secondMostExpensive.eq(2))
                .fetch();
    }
}

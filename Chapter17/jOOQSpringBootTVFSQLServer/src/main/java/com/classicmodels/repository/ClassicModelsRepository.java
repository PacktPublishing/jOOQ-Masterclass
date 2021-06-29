package com.classicmodels.repository;

import static jooq.generated.Tables.PRODUCT_OF_PRODUCT_LINE;
import jooq.generated.tables.ProductOfProductLine;
import static jooq.generated.tables.Productline.PRODUCTLINE;
import org.jooq.DSLContext;
import org.jooq.Result;
import static org.jooq.impl.DSL.val;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void selectFromTableValued() {

        ProductOfProductLine popl = new ProductOfProductLine();
        Result<?> r = ctx.fetch(popl.call("Trains").asTable());       
        System.out.println("Result:\n" + r);  
        
        ctx.selectFrom(PRODUCT_OF_PRODUCT_LINE(val("Trains"))).fetch();

        // using call()
        ctx.select(PRODUCT_OF_PRODUCT_LINE.P_ID, PRODUCT_OF_PRODUCT_LINE.P_NAME)
                .from(PRODUCT_OF_PRODUCT_LINE.call("Classic Cars"))
                .where(PRODUCT_OF_PRODUCT_LINE.P_ID.gt(100L))
                .fetch();
        
        // using val()
        ctx.select(PRODUCT_OF_PRODUCT_LINE.P_ID, PRODUCT_OF_PRODUCT_LINE.P_NAME)
                .from(PRODUCT_OF_PRODUCT_LINE(val("Classic Cars")))
                .where(PRODUCT_OF_PRODUCT_LINE.P_ID.gt(100L))
                .fetch();
    }

    public void crossApplyFromTableValued() {
        
        // using call()
        ctx.select(PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION, PRODUCT_OF_PRODUCT_LINE.asterisk())
                .from(PRODUCTLINE).crossApply(PRODUCT_OF_PRODUCT_LINE.call("Classic Cars"))
                .where(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT_OF_PRODUCT_LINE.P_LINE))
                .fetch();
        
        // using val()
        ctx.select(PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION, PRODUCT_OF_PRODUCT_LINE.asterisk())
                .from(PRODUCTLINE).crossApply(PRODUCT_OF_PRODUCT_LINE(val("Classic Cars")))
                .where(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT_OF_PRODUCT_LINE.P_LINE))
                .fetch();
    }
    
    @Transactional
    public void outerApplyFromTableValued() {
        
        ctx.insertInto(PRODUCTLINE, PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.CODE)
                .values("Helicopters", 855933L)
                .onDuplicateKeyIgnore()
                .execute();
        
        // since "Helicopters" has no products, CROSS APPLY will not fetch it        
        ctx.select(PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION, PRODUCT_OF_PRODUCT_LINE.asterisk())
                .from(PRODUCTLINE).crossApply(PRODUCT_OF_PRODUCT_LINE(PRODUCTLINE.PRODUCT_LINE))
                //.where(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT_OF_PRODUCT_LINE.P_LINE))
                .fetch();
        
        // OUTER APPLY returns "Helicopters" as well        
        ctx.select(PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION, PRODUCT_OF_PRODUCT_LINE.asterisk())
                .from(PRODUCTLINE).outerApply(PRODUCT_OF_PRODUCT_LINE(PRODUCTLINE.PRODUCT_LINE))
                //.where(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT_OF_PRODUCT_LINE.P_LINE))
                .fetch();
    }
}

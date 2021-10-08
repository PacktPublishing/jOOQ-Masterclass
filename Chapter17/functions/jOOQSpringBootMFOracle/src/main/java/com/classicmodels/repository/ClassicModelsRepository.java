package com.classicmodels.repository;

import java.math.BigDecimal;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.udt.ProductObj.PRODUCT_OBJ;
import jooq.generated.udt.records.ProductObjRecord;
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

    public void executeMemberFunction() {
        
        // ProductObjRecord po = new ProductObjRecord();        
        // po.attach(ctx.configuration());
        
        // ProductObjRecord po = PRODUCT_OBJ.newRecord();
        // po.attach(ctx.configuration());
        
        ProductObjRecord po = ctx.newRecord(PRODUCT_OBJ);
        
        po.setPrice(BigDecimal.valueOf(125.55));
        po.setMsrp(BigDecimal.valueOf(145.55));
        
        po.display();
        po.diff();
        ProductObjRecord incPo = po.increase(10);
        incPo.display();
        
        System.out.println("Price: " + po.getPrice());
        System.out.println("MSRP: " + po.getMsrp());
        System.out.println("Price (inc): " + incPo.getPrice());
        System.out.println("MSRP (inc): " + incPo.getMsrp());
        
        ctx.select(PRODUCT.PRODUCT_NAME, PRODUCT.BUY_PRICE)
                .from(PRODUCT)
                .where(PRODUCT.BUY_PRICE.lt(incPo.diff()))
                .fetch();
    }

}






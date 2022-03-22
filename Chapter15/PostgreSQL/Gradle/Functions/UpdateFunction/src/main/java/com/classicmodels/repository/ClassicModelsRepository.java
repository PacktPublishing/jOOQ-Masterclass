package com.classicmodels.repository;

import static jooq.generated.Routines.updateMsrp;
import jooq.generated.routines.UpdateMsrp;
import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.inline;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    @Transactional
    public void executeUpdateFunction() {

        // EXECUTION 1
        UpdateMsrp um = new UpdateMsrp();
        um.setId(1L);
        um.setDebit(10);
        um.execute(ctx.configuration());

        float newMsrp = um.getReturnValue();

        System.out.println("Msrp: " + newMsrp);     
        
        // EXECUTION 2
        ctx.select(PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME, 
                PRODUCT.MSRP.as("obsolete_msrp"), updateMsrp(PRODUCT.PRODUCT_ID, inline(50)))
                .from(PRODUCT)               
                .fetch();
    }
}

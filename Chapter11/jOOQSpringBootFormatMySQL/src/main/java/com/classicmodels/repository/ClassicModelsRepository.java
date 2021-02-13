package com.classicmodels.repository;

import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Productline.PRODUCTLINE;
import org.jooq.DSLContext;
import org.jooq.JSONFormat;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public String formatTableAsJSON() {
        
        String result = ctx.select(PRODUCTLINE.PRODUCT_LINE, PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME)
                .from(PRODUCTLINE)
                .join(PRODUCT)
                .onKey()
                .fetch()
                .formatJSON(JSONFormat.DEFAULT_FOR_RECORDS);
        System.out.println("EXAMPLE 1:\n" + result);
                
        return "";
    }
}

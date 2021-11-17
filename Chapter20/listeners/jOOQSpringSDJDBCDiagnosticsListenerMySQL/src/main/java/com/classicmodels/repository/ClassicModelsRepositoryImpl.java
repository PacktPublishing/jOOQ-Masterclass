package com.classicmodels.repository;

import static jooq.generated.tables.Productline.PRODUCTLINE;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepositoryImpl implements ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepositoryImpl(DSLContext ctx) {
        this.ctx = ctx;
    }   

    @Override
    @Transactional
    public void updateProductLineDescriptionJooq(String id) {

        ctx.update(PRODUCTLINE)
                .set(PRODUCTLINE.TEXT_DESCRIPTION, "Lorem ipsum dolor sit amet via jOOQ")
                .where(PRODUCTLINE.PRODUCT_LINE.eq(id))
                .execute();
    }        
}
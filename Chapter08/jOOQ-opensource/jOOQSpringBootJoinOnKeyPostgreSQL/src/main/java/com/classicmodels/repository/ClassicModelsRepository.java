package com.classicmodels.repository;

import static jooq.generated.tables.Productline.PRODUCTLINE;
import static jooq.generated.tables.Productlinedetail.PRODUCTLINEDETAIL;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.row;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    // EXAMPLE 1
    public void joinProductlineProductlinedetailViaOn() {

        System.out.println("EXAMPLE 1\n"
                + ctx.select(PRODUCTLINE.TEXT_DESCRIPTION, PRODUCTLINE.CREATED_ON,
                        PRODUCTLINEDETAIL.LINE_CAPACITY, PRODUCTLINEDETAIL.LINE_TYPE)
                        .from(PRODUCTLINE)
                        .innerJoin(PRODUCTLINEDETAIL)
                        .on(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCTLINEDETAIL.PRODUCT_LINE)
                                .and(PRODUCTLINE.CODE.eq(PRODUCTLINEDETAIL.CODE)))
                        .fetch()
        );
    }

    // EXAMPLE 2
    public void joinProductlineProductlinedetailViaOnRow() {

        System.out.println("EXAMPLE 2\n"
                + ctx.select(PRODUCTLINE.TEXT_DESCRIPTION, PRODUCTLINE.CREATED_ON,
                        PRODUCTLINEDETAIL.LINE_CAPACITY, PRODUCTLINEDETAIL.LINE_TYPE)
                        .from(PRODUCTLINE)
                        .innerJoin(PRODUCTLINEDETAIL)
                        .on(row(PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.CODE).eq(
                                row(PRODUCTLINEDETAIL.PRODUCT_LINE, PRODUCTLINEDETAIL.CODE)))
                        .fetch()
        );
    }

    // EXAMPLE 3
    public void joinProductlineProductlinedetailViaOnKey() {

        System.out.println("EXAMPLE 3\n"
                + ctx.select(PRODUCTLINE.TEXT_DESCRIPTION, PRODUCTLINE.CREATED_ON,
                        PRODUCTLINEDETAIL.LINE_CAPACITY, PRODUCTLINEDETAIL.LINE_TYPE)
                        .from(PRODUCTLINE)
                        .innerJoin(PRODUCTLINEDETAIL)
                        .onKey()
                        .fetch()
        );
    }

    // EXAMPLE 4
    public void joinProductlineProductlinedetailViaOnKeyFK() {

        System.out.println("EXAMPLE 4\n"
                + ctx.select(PRODUCTLINE.TEXT_DESCRIPTION, PRODUCTLINE.CREATED_ON,
                        PRODUCTLINEDETAIL.LINE_CAPACITY, PRODUCTLINEDETAIL.LINE_TYPE)
                        .from(PRODUCTLINE)
                        .innerJoin(PRODUCTLINEDETAIL)
                        .onKey(PRODUCTLINEDETAIL.PRODUCT_LINE, PRODUCTLINEDETAIL.CODE)
                        .fetch()
        );
    }
}
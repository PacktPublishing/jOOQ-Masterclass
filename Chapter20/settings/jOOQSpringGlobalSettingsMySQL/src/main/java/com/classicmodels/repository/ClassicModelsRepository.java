package com.classicmodels.repository;

import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Productline.PRODUCTLINE;
import org.jooq.DSLContext;
import org.jooq.conf.RenderKeywordCase;
import org.jooq.conf.Settings;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void selectProducts() {

        // notice that the schema name is not rendered (this is the effect of global settings from @Bean)
        ctx.select(PRODUCTLINE.PRODUCT_LINE, PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR)
                .from(PRODUCTLINE)
                .join(PRODUCT)
                .on(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                .fetch();

        // since Settings is instantiated and Configuration#set() is used, this instance
        // become the new global Settings, so the Settings from @Bean is no more in action
        ctx.configuration().set(
                new Settings()
                        .withMaxRows(5)) // JDBC silently drop the excess rows and keeps only 5
                .dsl()
                .select(PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR)
                .from(PRODUCT)
                .fetch();

        // the previous JDBC max rows were globally set, so it applies to the whole further queries (still 5 records are returned by JDBC)
        ctx.select(ORDER.ORDER_ID, ORDER.STATUS)
                .from(ORDER)
                .fetch();

        // append new global settings to the current global Settings (the current global Settings is the one instantiated previously)
        ctx.configuration().settings()
                .withRenderKeywordCase(RenderKeywordCase.UPPER); // render keywords (e.g., SELECT, FROM, ...) in upper case
        ctx.select(PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR)
                .from(PRODUCT)
                .fetch();

        // the result set contains 5 records and the rendered SQL contains upper case keywords (this happends for all further queries)
        ctx.select(ORDER.ORDER_ID, ORDER.STATUS)
                .from(ORDER)
                .fetch();
    }
}
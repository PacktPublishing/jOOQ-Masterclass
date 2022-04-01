package com.classicmodels.repository;

import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Productline.PRODUCTLINE;
import org.jooq.DSLContext;
import org.jooq.conf.MappedSchema;
import org.jooq.conf.RenderKeywordCase;
import org.jooq.conf.RenderMapping;
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

        // this query uses the default jOOQ Settings
        ctx.select(PRODUCTLINE.PRODUCT_LINE, PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR)
                .from(PRODUCTLINE)
                .join(PRODUCT)
                .on(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                .fetch();

        // since Settings is instantiated and Configuration#derive() is used, this instance
        // become the new local Settings, so is used ONLY for this query
        ctx.configuration().derive(
                new Settings()
                        .withMaxRows(5)) // JDBC silently drop the excess rows and keeps only 5
                .dsl()
                .select(PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR)
                .from(PRODUCT)
                .fetch();

        // the previous JDBC max rows were locally set, so it doesn't apply here
        ctx.select(ORDER.ORDER_ID, ORDER.STATUS)
                .from(ORDER)
                .fetch();
        
        // example of inheriting settings
        ctx.configuration().settings().withRenderMapping(new RenderMapping()
                .withSchemata(
                        new MappedSchema().withInput("classicmodels")
                                .withOutput("classicmodels_test")));
        
        System.out.println("SQL: " + ctx.select(ORDER.ORDER_ID, ORDER.STATUS)
                .from(ORDER)
                .getSQL());
        
        // 'derivedCtx' inherits settings of 'ctx'
        DSLContext derivedCtx = ctx.configuration().derive(
                ctx.settings() // using here new Settings() will NOT inherit 'ctx' settings
                        .withRenderKeywordCase(RenderKeywordCase.UPPER))
                .dsl();
        
        System.out.println("SQL: " + derivedCtx.select(ORDER.ORDER_ID, ORDER.STATUS)
                .from(ORDER)
                .getSQL());
    }
}

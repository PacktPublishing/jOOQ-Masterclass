package com.classicmodels.repository;

import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.table;
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
    public void selectProducts() {

        ctx.dropViewIfExists("product_view");
        ctx.createOrReplaceView("product_view").as(
                select(PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_LINE, PRODUCT.PRODUCT_NAME,
                        PRODUCT.CODE, PRODUCT.QUANTITY_IN_STOCK)
                        .from(PRODUCT)
                        .where(PRODUCT.QUANTITY_IN_STOCK.gt(5000))
        ).execute();

        // this insert passes the check
        ctx.insertInto(table("product_view"),
                PRODUCT.PRODUCT_LINE, PRODUCT.PRODUCT_NAME, PRODUCT.CODE, PRODUCT.QUANTITY_IN_STOCK)
                .values("Classic Cars", "Secret car", 599302L, 8200)
                .execute();

        // java.sql.SQLException: CHECK OPTION failed 'classicmodels.product_view'
        try {
            ctx.insertInto(table("product_view"),
                    PRODUCT.PRODUCT_LINE, PRODUCT.PRODUCT_NAME, PRODUCT.CODE, PRODUCT.QUANTITY_IN_STOCK)
                    .values("Classic Cars", "Secret car", 599302L, 4200)
                    .execute();
        } catch (org.jooq.exception.DataAccessException e) {
            System.out.println("Ooops! : " + e.getCause().getMessage());
        }
    }
}

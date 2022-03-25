package com.classicmodels.repository;

import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.query;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.update;
import org.jooq.impl.SQLDataType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void samples() {

        ctx.fetch("""
                  SELECT first_name, last_name 
                  FROM employee WHERE salary > ? AND job_title = ?
                  """, 5000, "Sales Rep");

        ctx.resultQuery("""
                        SELECT first_name, last_name 
                        FROM employee WHERE salary > ? AND job_title = ?
                        """, 5000, "Sales Rep")
                .fetch();

        ctx.query("""
                  UPDATE product SET product.quantity_in_stock = ? 
                      WHERE product.product_id = ?
                  """, 0, 2)
                .execute();
        
        // ctx.queries(query(""), query(""), query("")).executeBatch();
        
        ctx.select(field("FORMAT({0}, {1})", 123456789, "##-##-#####"))
                .fetch();

        ctx.select(field("DIFFERENCE({0}, {1})", SQLDataType.INTEGER, "Juice", "Jucy"))
                .fetch();
        
        ctx.batch(
                query("DECLARE @var1 VARCHAR(70)"),
                select(field("@var1=({0})", select(PRODUCT.PRODUCT_NAME)
                        .from(PRODUCT).where(PRODUCT.PRODUCT_ID.eq(1L)))),
                update(PRODUCT).set(PRODUCT.QUANTITY_IN_STOCK, 0)
                        .where(PRODUCT.PRODUCT_NAME.eq(field("@var1", String.class)))
        ).execute();

    }
}

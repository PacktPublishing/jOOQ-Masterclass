package com.classicmodels.repository;

import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.deleteFrom;
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
        
        String sql = ctx.resultQuery("{0} WHERE CURRENT OF cur", deleteFrom(PRODUCT)).getSQL();
        System.out.println("SQL: " + sql);
    }
}

package com.classicmodels.repository;

import java.math.BigDecimal;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Sale.SALE;
import static jooq.generated.tables.Token.TOKEN;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.coalesce;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectDistinct;
import static org.jooq.impl.DSL.with;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    // UPDATE and CTE
    public void cte1() {

        ctx.with("product_cte", "product_id", "max_buy_price")
                .as(select(ORDERDETAIL.PRODUCT_ID, max(ORDERDETAIL.PRICE_EACH))
                        .from(ORDERDETAIL)
                        .groupBy(ORDERDETAIL.PRODUCT_ID))
                .update(PRODUCT)
                .set(PRODUCT.BUY_PRICE, coalesce(field(select(field(name("max_buy_price"), BigDecimal.class))
                        .from(name("product_cte"))
                        .where(PRODUCT.PRODUCT_ID.eq(field(name("product_id"), Long.class)))), PRODUCT.BUY_PRICE))
                .execute();
    }

    // DELETE and CTE
    public void cte2() {

        ctx.with("token_cte", "t_sale_id")
                .as(selectDistinct(TOKEN.SALE_ID).from(TOKEN))
                .delete(SALE).where(SALE.SALE_ID.in(
                select(field(name("t_sale_id"), Long.class)).from(name("token_cte"))))
                .execute();

        ctx.deleteFrom(SALE).where(SALE.SALE_ID.in(with("token_cte", "t_sale_id")
                .as(selectDistinct(TOKEN.SALE_ID).from(TOKEN))
                .select(field(name("t_sale_id"), Long.class)).from(name("token_cte"))))
                .execute();
    }
}

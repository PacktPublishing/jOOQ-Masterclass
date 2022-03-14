package com.classicmodels.repository;

import java.math.BigDecimal;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Sale.SALE;
import static jooq.generated.tables.Token.TOKEN;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.cast;
import static org.jooq.impl.DSL.coalesce;
import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.constraint;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.max;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.rand;
import static org.jooq.impl.DSL.round;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectDistinct;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.unnest;
import org.jooq.impl.SQLDataType;
import static org.jooq.impl.SQLDataType.NUMERIC;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    // CTE and INSERT
    public void cte1() {

        ctx.dropTableIfExists("office_training").execute();
        ctx.createTable("office_training").as(
                select().from(OFFICE)).withNoData().execute();

        ctx.with("training_cte", "office_code")
                .as(select(OFFICE.OFFICE_CODE).from(OFFICE)
                        .orderBy(rand()).limit(10))
                .insertInto(table(name("office_training")))
                .select(select().from(OFFICE).where(OFFICE.OFFICE_CODE.notIn(
                        select(field(name("office_code"), String.class)).from(name("training_cte")))))
                .execute();
    }

    public void cte2() {

        ctx.dropTableIfExists("product_history").execute();
        ctx.createTable("product_history")
                .column("product_id", SQLDataType.BIGINT.nullable(false))
                .column("product_name", SQLDataType.VARCHAR(50).nullable(false))
                .column("buy_price", SQLDataType.DECIMAL.nullable(false))
                .column("market_rate", SQLDataType.VARCHAR(50).nullable(false))
                .constraints(
                        constraint("product_history_pk").primaryKey("product_id", "buy_price")
                ).execute();

        ctx.with("product_cte", "product_id", "product_name", "buy_price", "market_rate")
                .as(select(PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME,
                        PRODUCT.BUY_PRICE, inline("Present Price").as("market_rate"))
                        .from(PRODUCT)
                        .unionAll(select(PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME,
                                round(PRODUCT.BUY_PRICE.plus(PRODUCT.BUY_PRICE.mul(10).divide(100).plus(1)), 2).as("buy_price"),
                                inline("Future Price").as("market_rate"))
                                .from(PRODUCT)))
                .insertInto(table(name("product_history")))
                .select(select().from(name("product_cte")).orderBy(field(name("product_id"))))
                .execute();
    }

    // UPDATE and CTE
    public void cte3() {

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
    public void cte4() {

        ctx.with("token_cte", "t_sale_id")
                .as(selectDistinct(TOKEN.SALE_ID).from(TOKEN))
                .delete(SALE).where(SALE.SALE_ID.in(
                select(field(name("t_sale_id"), Long.class)).from(name("token_cte"))))
                .execute();
    }

    // MERGE and CTE
    public void cte5() {

        ctx.with("office_cte")
                .as(select(field(name("office_cte", "office_code")))
                        .from(unnest(new String[]{"1", "3", "5", "16", "17"}).as("office_cte", "office_code")))
                .insertInto(OFFICE)
                .columns(OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.PHONE, OFFICE.ADDRESS_LINE_FIRST,
                        OFFICE.COUNTRY, OFFICE.POSTAL_CODE, OFFICE.TERRITORY, OFFICE.INTERNAL_BUDGET)
                .select(select(field(name("office_code"), String.class),
                        inline("N/A"), inline("N/A"), inline("N/A"), inline("N/A"),
                        concat(inline("NA"), cast(rand().mul(899).plus(100), NUMERIC(3, 0)), inline("NA")), // generate some unique postal code
                        inline("N/A"), inline(0))
                        .from(name("office_cte")))
                .onConflict(OFFICE.OFFICE_CODE)
                .doUpdate()
                .set(OFFICE.INTERNAL_BUDGET, OFFICE.INTERNAL_BUDGET
                        .plus(OFFICE.INTERNAL_BUDGET.mul(10)).divide(100))
                .execute();
    }

    public void cte6() {

        ctx.with("office_cte")
                .as(select(field(name("office_cte", "office_code")))
                        .from(unnest(new String[]{"1", "3", "5", "16", "17"}).as("office_cte", "office_code")))
                .mergeInto(OFFICE)
                .using(table(name("office_cte")))
                .on(OFFICE.OFFICE_CODE.eq(field(name("office_cte", "office_code"), String.class)))
                .whenMatchedThenUpdate()
                .set(OFFICE.INTERNAL_BUDGET, OFFICE.INTERNAL_BUDGET
                        .plus(OFFICE.INTERNAL_BUDGET.mul(10)).divide(100))
                .whenNotMatchedThenInsert(OFFICE.OFFICE_CODE, OFFICE.CITY, OFFICE.PHONE, OFFICE.ADDRESS_LINE_FIRST,
                        OFFICE.COUNTRY, OFFICE.POSTAL_CODE, OFFICE.TERRITORY, OFFICE.INTERNAL_BUDGET)
                .values(field(name("office_code"), String.class),
                        inline("N/A"), inline("N/A"), inline("N/A"), inline("N/A"),
                        concat(inline("NA"), cast(rand().mul(899).plus(100), NUMERIC(3, 0)), inline("NA")), // generate some unique postal code
                        inline("N/A"), inline(0))
                .execute();
    }
}

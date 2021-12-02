package com.classicmodels.repository;

import java.util.stream.Collectors;
import static jooq.generated.Routines.getAvgPriceByProductLine;
import static jooq.generated.Routines.salePrice;
import jooq.generated.routines.SalePrice;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.in;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.sum;
import static org.jooq.impl.DSL.val;
import static org.jooq.impl.SQLDataType.DOUBLE;
import static org.jooq.impl.SQLDataType.INTEGER;
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
    public void queriesAndRoutines() {

        ctx.select(ORDERDETAIL.ORDER_ID, ORDERDETAIL.QUANTITY_ORDERED,
                PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME)
                .from(ORDERDETAIL)
                .join(PRODUCT)
                .on(ORDERDETAIL.PRODUCT_ID.eq(PRODUCT.PRODUCT_ID))
                .fetch();

        ctx.insertInto(PRODUCT, PRODUCT.PRODUCT_LINE, PRODUCT.PRODUCT_NAME, PRODUCT.CODE)
                .values("Classic Cars", "Super Car", 599302L)
                .execute();

        ctx.update(PRODUCT)
                .set(PRODUCT.PRODUCT_NAME, "Amazing Car")
                .where(PRODUCT.PRODUCT_NAME.eq("Super Car"))
                .execute();

        ctx.deleteFrom(PRODUCT)
                .where(PRODUCT.PRODUCT_NAME.eq("Amazing Car"))
                .execute();

        ctx.select(PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME)
                .from(PRODUCT)
                .where(PRODUCT.QUANTITY_IN_STOCK.gt(5000))
                .fetch();

        ctx.resultQuery("SELECT * FROM product WHERE product.product_id < 5").fetch();

        ctx.batch(
                ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(2005, 1370L, 1282.64, 1, 15.55),
                ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(2004, 1370L, 3938.24, 2, 22.33),
                ctx.insertInto(SALE, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(2004, 1370L, 4676.14, 3, 10.2),
                // not logged
                ctx.update(SALE).set(SALE.FISCAL_YEAR, 2004).where(SALE.FISCAL_YEAR.eq(2003))
        ).execute();

        ctx.selectFrom(PRODUCT)
                .collect(Collectors.toList());

        getAvgPriceByProductLine(ctx.configuration(), "Classic Cars");

        SalePrice sp = new SalePrice();
        sp.set(in("quantity", INTEGER), 25);
        sp.set(in("list_price", DOUBLE), 15.5);
        sp.set(in("fraction_of_price", DOUBLE), 0.75);
        sp.execute(ctx.configuration());

        ctx.select(ORDERDETAIL.ORDER_ID,
                sum(salePrice(ORDERDETAIL.QUANTITY_ORDERED,
                        ORDERDETAIL.PRICE_EACH.coerce(Double.class), val(0.75))).as("sum_sale_price"))
                .from(ORDERDETAIL)
                .groupBy(ORDERDETAIL.ORDER_ID)
                .orderBy(field(name("sum_sale_price")).desc())
                .fetch();
    }
}

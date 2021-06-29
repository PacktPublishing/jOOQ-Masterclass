package com.classicmodels.repository;

import java.math.BigDecimal;
import jooq.generated.Routines;
import static jooq.generated.Routines.netPriceEach;
import jooq.generated.routines.NetPriceEach;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.DSLContext;
import org.jooq.Field;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.sum;
import static org.jooq.impl.DSL.val;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void executeScalarFunction() {

        // EXECUTION 1
        NetPriceEach npe1 = new NetPriceEach();
        npe1.setQuantity(25);
        npe1.setListPrice(BigDecimal.valueOf(15.5));
        npe1.setDiscount(BigDecimal.valueOf(0.75));

        npe1.execute(ctx.configuration());

        System.out.println("Execution 1: " + npe1.getReturnValue());

        // EXECUTION 2
        NetPriceEach npe2 = new NetPriceEach();
        npe2.setQuantity(field(select(PRODUCT.QUANTITY_IN_STOCK)
                .from(PRODUCT).where(PRODUCT.PRODUCT_ID.eq(1L))));
        npe2.setListPrice(field(select(PRODUCT.MSRP)
                .from(PRODUCT).where(PRODUCT.PRODUCT_ID.eq(1L))));
        npe2.setDiscount(BigDecimal.valueOf(0.75));

        System.out.println("Execution 2:\n"
                + ctx.fetchValue(npe2.asField("netPriceEach"))); // or, ctx.select(npe2.asField("netPriceEach")).fetch()

        // EXECUTION 3
        BigDecimal npe3 = Routines.netPriceEach(
                ctx.configuration(), 25, BigDecimal.valueOf(15.5), BigDecimal.valueOf(0.75));

        System.out.println("Execution 3: " + npe3);

        // EXECUTION 4
        Field<BigDecimal> npe4 = Routines.netPriceEach(
                field(select(PRODUCT.QUANTITY_IN_STOCK)
                        .from(PRODUCT).where(PRODUCT.PRODUCT_ID.eq(1L))),
                field(select(PRODUCT.MSRP)
                        .from(PRODUCT).where(PRODUCT.PRODUCT_ID.eq(1L))),
                val(BigDecimal.valueOf(0.75)));

        System.out.println("Execution 4:\n"
                + ctx.fetchValue(npe4)); // or, ctx.select(npe4).fetch()

        // EXECUTION 5
        ctx.select(ORDERDETAIL.ORDER_ID,
                sum(netPriceEach(ORDERDETAIL.QUANTITY_ORDERED.coerce(Integer.class),
                        ORDERDETAIL.PRICE_EACH, val(0.75))).as("sum_net_price"))
                .from(ORDERDETAIL)
                .groupBy(ORDERDETAIL.ORDER_ID)
                .orderBy(field(name("sum_net_price")).desc())
                .fetch();
    }

}

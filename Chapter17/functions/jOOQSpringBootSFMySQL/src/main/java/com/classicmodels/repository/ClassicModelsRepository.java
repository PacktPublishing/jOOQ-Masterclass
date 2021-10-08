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
import static org.jooq.impl.DSL.in;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.select;
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

    public void executeScalarFunction() {

        // EXECUTION 1
        NetPriceEach npe1 = new NetPriceEach();
        npe1.setQuantity(25);
        npe1.setListPrice(15.5);
        npe1.setDiscount(0.75);

        npe1.execute(ctx.configuration());
        
        System.out.println("Execution 1: " + npe1.getReturnValue());

        // EXECUTION 2
        NetPriceEach npe2 = new NetPriceEach();
        npe2.set(in("quantity", INTEGER), 25);
        npe2.set(in("list_price", DOUBLE), 15.5);
        npe2.set(in("discount", DOUBLE), 0.75);

        npe2.execute(ctx.configuration());

        System.out.println("Execution 2: " + npe2.getReturnValue());

        // EXECUTION 3
        NetPriceEach npe3 = new NetPriceEach();
        npe3.setQuantity(field(select(PRODUCT.QUANTITY_IN_STOCK)
                .from(PRODUCT).where(PRODUCT.PRODUCT_ID.eq(1L))));
        npe3.setListPrice(field(select(PRODUCT.MSRP.coerce(Double.class))
                .from(PRODUCT).where(PRODUCT.PRODUCT_ID.eq(1L))));
        npe3.setDiscount(0.75);

        System.out.println("Execution 3:\n"
                + ctx.fetchValue(npe3.asField("netPriceEach"))); // or, ctx.select(npe3.asField("netPriceEach")).fetch()

        // EXECUTION 4
        double npe4 = Routines.netPriceEach(
                ctx.configuration(), 25, 15.5, 0.75);

        System.out.println("Execution 4: " + npe4);

        // EXECUTION 5
        Field<Double> npe5 = Routines.netPriceEach(
                field(select(PRODUCT.QUANTITY_IN_STOCK)
                        .from(PRODUCT).where(PRODUCT.PRODUCT_ID.eq(1L))),
                field(select(PRODUCT.MSRP.coerce(Double.class))
                        .from(PRODUCT).where(PRODUCT.PRODUCT_ID.eq(1L))),
                val(0.75));

        System.out.println("Execution 5:\n"
                + ctx.fetchValue(npe5)); // or, ctx.select(npe5).fetch()

        // EXECUTION 6
        ctx.select(ORDERDETAIL.ORDER_ID,
                sum(netPriceEach(ORDERDETAIL.QUANTITY_ORDERED,
                        ORDERDETAIL.PRICE_EACH.coerce(Double.class), val(0.75))).as("sum_net_price"))
                .from(ORDERDETAIL)
                .groupBy(ORDERDETAIL.ORDER_ID)
                .orderBy(field(name("sum_net_price")).desc())
                .fetch();
    }
}

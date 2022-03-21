package com.classicmodels.repository;

import jooq.generated.Routines;
import static jooq.generated.Routines.salePrice;
import jooq.generated.routines.SalePrice;
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
@Transactional
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void executeScalarFunction() {

        // EXECUTION 1
        SalePrice sp1 = new SalePrice();
        sp1.setQuantity(25);
        sp1.setListPrice(15.5);
        sp1.setFractionOfPrice(0.75);

        sp1.execute(ctx.configuration());
        
        System.out.println("Execution 1: " + sp1.getReturnValue());

        // EXECUTION 2
        SalePrice sp2 = new SalePrice();
        sp2.set(in("quantity", INTEGER), 25);
        sp2.set(in("list_price", DOUBLE), 15.5);
        sp2.set(in("fraction_of_price", DOUBLE), 0.75);

        sp2.execute(ctx.configuration());

        System.out.println("Execution 2: " + sp2.getReturnValue());

        // EXECUTION 3
        SalePrice sp3 = new SalePrice();
        sp3.setQuantity(field(select(PRODUCT.QUANTITY_IN_STOCK)
                .from(PRODUCT).where(PRODUCT.PRODUCT_ID.eq(1L))));
        sp3.setListPrice(field(select(PRODUCT.MSRP.coerce(Double.class))
                .from(PRODUCT).where(PRODUCT.PRODUCT_ID.eq(1L))));
        sp3.setFractionOfPrice(0.75);

        System.out.println("Execution 3:\n"
                + ctx.fetchValue(sp3.asField("sale_price"))); // or, ctx.select(sp3.asField("sale_price")).fetch()

        // EXECUTION 4
        double sp4 = Routines.salePrice(
                ctx.configuration(), 25, 15.5, 0.75);

        System.out.println("Execution 4: " + sp4);

        // EXECUTION 5
        Field<Double> sp51 = Routines.salePrice(
                field(select(PRODUCT.QUANTITY_IN_STOCK)
                        .from(PRODUCT).where(PRODUCT.PRODUCT_ID.eq(1L))),
                field(select(PRODUCT.MSRP.coerce(Double.class))
                        .from(PRODUCT).where(PRODUCT.PRODUCT_ID.eq(1L))),
                val(0.75));

        // ctx.select(sp51).fetch(), or
        
        double sp52 = ctx.fetchValue(salePrice(
                field(select(PRODUCT.QUANTITY_IN_STOCK)
                        .from(PRODUCT).where(PRODUCT.PRODUCT_ID.eq(1L))),
                field(select(PRODUCT.MSRP.coerce(Double.class))
                        .from(PRODUCT).where(PRODUCT.PRODUCT_ID.eq(1L))),
                val(0.75))); 
                
        System.out.println("Execution 5:\n" + sp52);

        // EXECUTION 6
        ctx.select(ORDERDETAIL.ORDER_ID,
                sum(salePrice(ORDERDETAIL.QUANTITY_ORDERED,
                        ORDERDETAIL.PRICE_EACH.coerce(Double.class), val(0.75))).as("sum_sale_price"))
                .from(ORDERDETAIL)
                .groupBy(ORDERDETAIL.ORDER_ID)
                .orderBy(field(name("sum_sale_price")).desc())
                .fetch();
    }
}

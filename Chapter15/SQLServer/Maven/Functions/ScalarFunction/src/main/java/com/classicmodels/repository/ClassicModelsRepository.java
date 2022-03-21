package com.classicmodels.repository;

import jooq.generated.Routines;
import static jooq.generated.Routines.salePrice;
import jooq.generated.routines.SalePrice;
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
        SalePrice sp1 = new SalePrice();
        sp1.setQuantity(25);
        sp1.setListPrice(15.5f);
        sp1.setFractionOfPrice(0.75f);

        sp1.execute(ctx.configuration());

        System.out.println("Execution 1: " + sp1.getReturnValue());

        // EXECUTION 2
        SalePrice sp2 = new SalePrice();
        sp2.setQuantity(field(select(PRODUCT.QUANTITY_IN_STOCK)
                .from(PRODUCT).where(PRODUCT.PRODUCT_ID.eq(1L))));
        sp2.setListPrice(field(select(PRODUCT.MSRP.coerce(Float.class))
                .from(PRODUCT).where(PRODUCT.PRODUCT_ID.eq(1L))));
        sp2.setFractionOfPrice(0.75f);

        System.out.println("Execution 2:\n"
                + ctx.fetchValue(sp2.asField("sale_price"))); // or, ctx.select(sp2.asField("sale_price")).fetch()

        // EXECUTION 3
        double sp3 = Routines.salePrice(
                ctx.configuration(), 25, 15.5f, 0.75f);

        System.out.println("Execution 3: " + sp3);

        // EXECUTION 4
        Field<Float> sp41 = Routines.salePrice(
                field(select(PRODUCT.QUANTITY_IN_STOCK)
                        .from(PRODUCT).where(PRODUCT.PRODUCT_ID.eq(1L))),
                field(select(PRODUCT.MSRP.coerce(Float.class))
                        .from(PRODUCT).where(PRODUCT.PRODUCT_ID.eq(1L))),
                val(0.75f));

        // ctx.select(sp41).fetch(), or
        
        float sp42 = ctx.fetchValue(salePrice(
                field(select(PRODUCT.QUANTITY_IN_STOCK)
                        .from(PRODUCT).where(PRODUCT.PRODUCT_ID.eq(1L))),
                field(select(PRODUCT.MSRP.coerce(Float.class))
                        .from(PRODUCT).where(PRODUCT.PRODUCT_ID.eq(1L))),
                val(0.75f))); 
                
        System.out.println("Execution 4:\n" + sp42);

        // EXECUTION 5
        ctx.select(ORDERDETAIL.ORDER_ID,
                sum(salePrice(ORDERDETAIL.QUANTITY_ORDERED,
                        ORDERDETAIL.PRICE_EACH.coerce(Float.class), val(0.75f))).as("sum_sale_price"))
                .from(ORDERDETAIL)
                .groupBy(ORDERDETAIL.ORDER_ID)
                .orderBy(field(name("sum_sale_price")).desc())
                .fetch();
    }

}

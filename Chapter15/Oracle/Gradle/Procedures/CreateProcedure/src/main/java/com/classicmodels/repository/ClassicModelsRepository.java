package com.classicmodels.repository;

import java.math.BigDecimal;
import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.DSLContext;
import org.jooq.Parameter;
import static org.jooq.impl.DSL.avg;
import static org.jooq.impl.DSL.in;
import static org.jooq.impl.DSL.out;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.SQLDataType.DECIMAL;
import static org.jooq.impl.SQLDataType.VARCHAR;
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
    public void createProcedure() {

        Parameter<String> pl = in("pl", VARCHAR);
        Parameter<BigDecimal> average = out("average", DECIMAL);

        // or, use ctx.dropProcedureIfExists() and createProcedure()
        ctx.createOrReplaceProcedure("get_avg_price_by_product_line_jooq")
                .parameters(
                        pl, average
                )
                .as(select(avg(PRODUCT.BUY_PRICE)).into(average)
                        .from(PRODUCT)
                        .where(PRODUCT.PRODUCT_LINE.eq(pl)))
                .execute();
    }

    public void callProcedure() {

        // calling the previously created procedures via the generated code
        
        /*
        // EXECUTION 1
        SalePriceJooq sp = new SalePriceJooq();
        sp.setListPrice(23.44);
        sp.setFractionOfPrice(0.25);
        sp.setQuantity(100);

        sp.execute(ctx.configuration());

        Double result1 = sp.getReturnValue();
        System.out.println("Result: " + result1);

        // EXECUTION 2
        Double result2 = salePriceJooq(ctx.configuration(), 100, 23.44, 0.25);
        System.out.println("Avg: " + result2);

        // EXECUTION 3         
        ctx.select(PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME, PRODUCT.BUY_PRICE)
                .from(PRODUCT)
                .where(PRODUCT.BUY_PRICE.coerce(Double.class)
                        .gt(salePriceJooq(ctx.configuration(), 1, 23.44, 0.25))
                        .and(PRODUCT.PRODUCT_LINE.eq("Classic Cars")))
                .fetch();       
        */
    }   
}

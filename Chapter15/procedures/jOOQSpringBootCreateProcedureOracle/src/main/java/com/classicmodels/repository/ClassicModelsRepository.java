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
        GetAvgPriceByProductLineJooq avg = new GetAvgPriceByProductLineJooq();
        avg.setPl("Classic Cars");

        avg.execute(ctx.configuration());

        BigInteger result1 = avg.getAverage();
        System.out.println("Avg: " + result1);

        // EXECUTION 2
        BigInteger result2 = getAvgPriceByProductLine(ctx.configuration(), "Classic Cars");
        System.out.println("Avg: " + result2);

        // EXECUTION 3         
        ctx.select(PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME, PRODUCT.BUY_PRICE)
                .from(PRODUCT)
                .where(PRODUCT.BUY_PRICE.coerce(BigInteger.class).gt(getAvgPriceByProductLine(
                        ctx.configuration(), "Classic Cars"))
                        .and(PRODUCT.PRODUCT_LINE.eq("Classic Cars")))
                .fetch();
        */
    }   
}

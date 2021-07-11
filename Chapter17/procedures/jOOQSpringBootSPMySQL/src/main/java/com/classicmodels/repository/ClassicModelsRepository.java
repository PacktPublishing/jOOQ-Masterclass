package com.classicmodels.repository;

import static jooq.generated.Routines.getAvgPriceByProductLine;
import static jooq.generated.Routines.getProduct;
import static jooq.generated.Routines.setCounter;
import jooq.generated.routines.GetAvgPriceByProductLine;
import jooq.generated.routines.GetEmpsInOffice;
import jooq.generated.routines.GetProduct;
import jooq.generated.routines.SetCounter;
import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.Record;
import org.jooq.Results;
import org.jooq.Table;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.val;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void executeStoredProcedureInAndOut() {

        // EXECUTION 1
        GetAvgPriceByProductLine avg = new GetAvgPriceByProductLine();
        avg.setPl("Classic Cars");

        avg.execute(ctx.configuration());

        System.out.println("Avg: " + avg.getAverage());

        // EXECUTION 2
        ctx.fetchValue(val(getAvgPriceByProductLine(
                ctx.configuration(), "Classic Cars")));

        // EXECUTION 3
        ctx.select(PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME, PRODUCT.BUY_PRICE)
                .from(PRODUCT)
                .where(PRODUCT.BUY_PRICE.gt(getAvgPriceByProductLine(
                        ctx.configuration(), "Classic Cars"))
                        .and(PRODUCT.PRODUCT_LINE.eq("Classic Cars")))
                .fetch();
    }

    public void executeStoredProcedureInOut() {

        // EXECUTION 1
        SetCounter c = new SetCounter();
        c.setCounter(100);
        c.setInc(10);

        c.execute(ctx.configuration());

        System.out.println("Counter: " + c.getCounter());

        // EXECUTION 2
        ctx.insertInto(PRODUCT, PRODUCT.PRODUCT_ID, PRODUCT.CODE)
                .values((long) setCounter(ctx.configuration(), 10000, (int) (Math.random() * 1000)),
                        542123L)
                .execute();
    }

    public void executeStoredProcedureSelect() {

        // EXECUTION 1
        GetProduct gp = new GetProduct();
        gp.setPid(1L);

        gp.execute(ctx.configuration());
        System.out.println("Result: \n" + gp.getResults().get(0)); // Result<Record>

        // EXECUTION 2
        getProduct(ctx.configuration(), 1L);

        // EXECUTION 3
        Table<?> t = table(gp.getResults().get(0));
        ctx.selectFrom(t).fetch();
    }

    public void executeStoredProcedureMultipleSelect() {

        // EXECUTION 1
        GetEmpsInOffice geio = new GetEmpsInOffice();
        geio.setInOfficeCode("1");

        geio.execute(ctx.configuration());

        Results results = geio.getResults();

        for (Result<?> result : results) {            
            System.out.println("Result set:\n");
            for (Record record : result) {
                System.out.println(record);
            }
        }                        
    }
}

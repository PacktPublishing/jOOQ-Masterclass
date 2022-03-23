package com.classicmodels.repository;

import java.math.BigInteger;
import static jooq.generated.Routines.getAvgPriceByProductLine;
import static jooq.generated.Routines.getEmpsInOffice;
import static jooq.generated.Routines.getProduct;
import static jooq.generated.Routines.setCounter;
import jooq.generated.routines.GetAvgPriceByProductLine;
import jooq.generated.routines.GetEmpsInOffice;
import jooq.generated.routines.GetProduct;
import jooq.generated.routines.SetCounter;
import static jooq.generated.tables.Product.PRODUCT;
import jooq.generated.tables.records.ProductRecord;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.Record;
import org.jooq.Results;
import org.jooq.Table;
import static org.jooq.impl.DSL.call;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.table;
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

    public void executeStoredProcedureInAndOut() {

        // EXECUTION 1
        GetAvgPriceByProductLine avg = new GetAvgPriceByProductLine();
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
    }

    public void executeStoredProcedureInOut() {

        // EXECUTION 1
        SetCounter c = new SetCounter();
        c.setCounter(BigInteger.valueOf(100));
        c.setInc(BigInteger.valueOf(10));

        c.execute(ctx.configuration());

        BigInteger result1 = c.getCounter();
        System.out.println("Counter: " + result1);

        // EXECUTION 2
        BigInteger result2 = setCounter(ctx.configuration(), BigInteger.valueOf(99), BigInteger.ONE);
        System.out.println("Counter: " + result2);

        // EXECUTION 3
        ctx.insertInto(PRODUCT, PRODUCT.PRODUCT_ID, PRODUCT.CODE)
                .values(setCounter(ctx.configuration(), BigInteger.valueOf(10000),
                        BigInteger.valueOf((int) (Math.random() * 1000))).longValue(),
                        542123L)
                .execute();
    }

    public void executeStoredProcedureSelect() {

        // EXECUTION 1
        GetProduct gp = new GetProduct();
        gp.setPid(1L);

        gp.execute(ctx.configuration());
                
        Result<Record> result1 = gp.getCursorResult();
        System.out.println("Result (1): \n" + result1);        

        // EXECUTION 2
        Result<Record> result2 = getProduct(ctx.configuration(), 1L); 
        System.out.println("Result (2): \n" + result2);        

        // EXECUTION 3
        // Table<?> t = table(gp.getResults().get(0)); // or, getProduct(ctx.configuration(), 1L)
        Table<ProductRecord> t = table(gp.getCursorResult().into(PRODUCT)); // or, getProduct(ctx.configuration(), 1L).into(PRODUCT)
        ctx.selectFrom(t).fetch();
    }

    public void executeStoredProcedureMultipleSelect() {

        // EXECUTION 1
        GetEmpsInOffice geio = new GetEmpsInOffice();
        geio.setInOfficeCode("1");

        geio.execute(ctx.configuration());

        Results results1 = geio.getResults();
        
        Result<Record> co1 = geio.getCursorOffice();
        Result<Record> ce1 = geio.getCursorEmployee();
        
        System.out.println("Office:\n" + co1);
        System.out.println("Employee:\n" + ce1);
        
        // EXECUTION 2
        GetEmpsInOffice results2 = getEmpsInOffice(ctx.configuration(), "1");        
        
        Result<Record> co2 = results2.getCursorOffice();
        Result<Record> ce2 = results2.getCursorEmployee();
        
        System.out.println("Office:\n" + co2);
        System.out.println("Employee:\n" + ce2);

        for (Result<Record> result : results1) { // or, results2.getResults()
            System.out.println("Result set:\n");
            for (Record record : result) {
                System.out.println(record);
            }
        }        
    }

    public void executeStoredProcedureViaCallStatement() {
        
        // CALL statement in an anonymous block
        ctx.begin(call(name("REFRESH_TOP3_PRODUCT"))
                .args(val("Trains")))
                .execute();

        // CALL statement directly
        ctx.call(name("REFRESH_TOP3_PRODUCT"))
                .args(val("Trains"))
                .execute();
    }
}
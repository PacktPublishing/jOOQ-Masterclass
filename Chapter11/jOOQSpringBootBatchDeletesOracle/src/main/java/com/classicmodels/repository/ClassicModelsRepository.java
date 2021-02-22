package com.classicmodels.repository;

import com.classicmodels.pojo.SimpleSale;
import java.math.BigInteger;
import java.util.Arrays;
import static jooq.generated.tables.BankTransaction.BANK_TRANSACTION;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Payment.PAYMENT;
import static jooq.generated.tables.Productlinedetail.PRODUCTLINEDETAIL;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import java.util.List;
import org.jooq.BatchBindStep;
import org.jooq.conf.Settings;
import static org.jooq.impl.DSL.select;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    @Transactional
    public void batchDeleteStatements() {

        // batch deletes (several queries)
        int[] result1 = ctx.configuration().derive(
                new Settings().withBatchSize(2))
                .dsl().batch(
                        ctx.deleteFrom(ORDERDETAIL)
                                .where(ORDERDETAIL.ORDER_ID.in(
                                        select(ORDER.ORDER_ID).from(ORDER)
                                                .where(ORDER.CUSTOMER_NUMBER.eq(103L)))),
                        ctx.deleteFrom(ORDER)
                                .where(ORDER.CUSTOMER_NUMBER.eq(103L)),
                        ctx.deleteFrom(CUSTOMERDETAIL)
                                .where(CUSTOMERDETAIL.CUSTOMER_NUMBER.eq(103L)),
                        ctx.deleteFrom(BANK_TRANSACTION)
                                .where(BANK_TRANSACTION.CUSTOMER_NUMBER.eq(103L)),
                        ctx.deleteFrom(PAYMENT)
                                .where(PAYMENT.CUSTOMER_NUMBER.eq(103L)),
                        ctx.deleteFrom(CUSTOMER)
                                .where(CUSTOMER.CUSTOMER_NUMBER.eq(103L)))
                .execute();

        System.out.println("EXAMPLE 1.1: " + Arrays.toString(result1));

        // batch deletes (single query)
        int[] result2 = ctx.batch(
                ctx.deleteFrom(SALE)
                        .where(SALE.SALE_.between((Double) null, (Double) null)))
                .bind(0, 1000)
                .bind(2000, 3000)
                .bind(4000, 5000)
                .execute();

        System.out.println("EXAMPLE 1.2: " + Arrays.toString(result2));
    }
    
    @Transactional
    public void batchDeleteOrder() {

        // avoid (if possible) - 3 batches
        int[] result1 = ctx.batch(
                ctx.deleteFrom(SALE)
                        .where(SALE.SALE_ID.eq(BigInteger.valueOf(10))),
                ctx.deleteFrom(SALE)
                        .where(SALE.SALE_ID.eq(BigInteger.valueOf(11)).and(SALE.SALE_.eq(0.0))),
                ctx.deleteFrom(SALE)
                        .where(SALE.SALE_ID.eq(BigInteger.valueOf(12)))
        ).execute();

        System.out.println("EXAMPLE 2.1: " + Arrays.toString(result1));

        // prefer - 2 batches
        int[] result2 = ctx.batch(
                ctx.deleteFrom(SALE)
                        .where(SALE.SALE_ID.eq(BigInteger.valueOf(11)).and(SALE.SALE_.eq(0.0))),
                ctx.deleteFrom(SALE)
                        .where(SALE.SALE_ID.eq(BigInteger.valueOf(10))),                
                ctx.deleteFrom(SALE)
                        .where(SALE.SALE_ID.eq(BigInteger.valueOf(12)))
        ).execute();

        System.out.println("EXAMPLE 2.2: " + Arrays.toString(result2));
    }
    
    public void batchDeleteRecords1() {
    
        var r1 = ctx.selectFrom(SALE)
                .where(SALE.SALE_ID.eq(BigInteger.valueOf(2)))
                .fetchOne();

        var r2 = ctx.selectFrom(SALE)
                .where(SALE.SALE_ID.eq(BigInteger.valueOf(3)))
                .fetchOne();
        
        var r3 = ctx.selectFrom(SALE)
                .where(SALE.SALE_ID.eq(BigInteger.valueOf(4)))
                .fetchOne();

        // There is a single batch since the generated SQL with bind variables is the same for r1-r3.
        // The order of records is preserved.
        int[] result = ctx.configuration().derive(
                new Settings().withBatchSize(3)).dsl()
                .batchDelete(r1, r2, r3)
                .execute();

        System.out.println("EXAMPLE 3.1: " + Arrays.toString(result));
    }    
    
    public void batchDeleteRecords2() {
    
        var r1 = ctx.selectFrom(SALE)
                .where(SALE.SALE_ID.eq(BigInteger.valueOf(6)))
                .fetchOne();

        var r2 = ctx.selectFrom(BANK_TRANSACTION)
                .where(BANK_TRANSACTION.TRANSACTION_ID.eq(2L))
                .fetchOne();
        
        var r3 = ctx.selectFrom(SALE)
                .where(SALE.SALE_ID.eq(BigInteger.valueOf(7)))
                .fetchOne();

        // There are two batches, one for r1 abd r3, and one r2
        // The order of records is not preserved (check the log).
        int[] result = ctx.configuration().derive(
                new Settings().withBatchSize(3)).dsl()
                .batchDelete(r1, r2, r3)
                .execute();

        System.out.println("EXAMPLE 3.2: " + Arrays.toString(result));
    }    
    
    public void batchDeleteRecords3() {
    
        var r1 = ctx.selectFrom(SALE)
                .where(SALE.SALE_ID.eq(BigInteger.valueOf(1)))
                .fetchOne();

        var r2 = ctx.selectFrom(BANK_TRANSACTION)
                .where(BANK_TRANSACTION.TRANSACTION_ID.eq(1L))
                .fetchOne();

        var r3 = ctx.selectFrom(PRODUCTLINEDETAIL)
                .where(PRODUCTLINEDETAIL.PRODUCT_LINE.eq("Classic Cars"))
                .fetchOne();

        // There are three batches, one for r1, one for r2, and one for r3 because the generated SQL with bind variables is not the same.
        // The order of records is preserved.
        int[] result = ctx.configuration().derive(
                new Settings().withBatchSize(3)).dsl()
                .batchDelete(r1, r2, r3)
                .execute();

        System.out.println("EXAMPLE 3.3: " + Arrays.toString(result));
    }    
    
    // batch collection of Objects    
    public void batchDeleteCollectionOfObjects() {

        List<SimpleSale> sales = List.of(
                new SimpleSale(2005, 1370L, 1282.64),
                new SimpleSale(2004, 1370L, 3938.24),
                new SimpleSale(2004, 1370L, 4676.14)
        );

        BatchBindStep batch = ctx.batch(
                ctx.delete(SALE)
                .where(SALE.FISCAL_YEAR.eq((BigInteger) null)
                .and(SALE.EMPLOYEE_NUMBER.eq((Long) null))
                .and(SALE.SALE_.eq((Double) null)))
        );

        sales.forEach(s -> batch.bind(s.getFiscalYear(), s.getEmployeeNumber(), s.getSale()));
        batch.execute();
    }
}
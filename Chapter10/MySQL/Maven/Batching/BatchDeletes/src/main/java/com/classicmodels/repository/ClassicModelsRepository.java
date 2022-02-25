package com.classicmodels.repository;

import com.classicmodels.pojo.SimpleSale;
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
import org.jooq.conf.StatementType;
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

    public void batchDeleteStatements() {

        // batch deletes (several queries)
        int[] result1 = ctx.batch(
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

        // batch deletes (single query) PreparedStatement
        int[] result21 = ctx.batch(
                ctx.deleteFrom(SALE)
                        .where(SALE.SALE_.between((Double) null, (Double) null)))
                .bind(0, 1000)
                .bind(2000, 3000)
                .bind(4000, 5000)
                .execute();

        System.out.println("EXAMPLE 1.2.1: " + Arrays.toString(result21));

        // batch deletes (single query) Statement
        int[] result22 = ctx.configuration().derive(
                new Settings().withStatementType(StatementType.STATIC_STATEMENT))
                .dsl().batch(
                        ctx.deleteFrom(SALE)
                                .where(SALE.SALE_.between((Double) null, (Double) null)))
                .bind(0, 1000)
                .bind(2000, 3000)
                .bind(4000, 5000)
                .execute();

        System.out.println("EXAMPLE 1.2.2: " + Arrays.toString(result22));
    }

    public void batchDeleteRecords1() {

        var r1 = ctx.selectFrom(SALE)
                .where(SALE.SALE_ID.eq(2L))
                .fetchOne();

        var r2 = ctx.selectFrom(SALE)
                .where(SALE.SALE_ID.eq(3L))
                .fetchOne();

        var r3 = ctx.selectFrom(SALE)
                .where(SALE.SALE_ID.eq(4L))
                .fetchOne();

        // There is a single batch since the generated SQL with bind variables is the same for r1-r3.
        // The order of records is preserved.
        
        // Records batch deletes (single query, bind values)
        if (r1 != null && r2 != null && r3 != null) {
            int[] result = ctx.batchDelete(r1, r2, r3)
                    .execute();

            // Records batch deletes (multiple query, inlined values)
            // Order of records is always preserved entirely
            // ctx.configuration().derive(
            //        new Settings().withStatementType(StatementType.STATIC_STATEMENT))
            //        .dsl().batchDelete(...) ...
            System.out.println("EXAMPLE 2: " + Arrays.toString(result));
        }
    }

    public void batchDeleteRecords2() {

        var r1 = ctx.selectFrom(SALE)
                .where(SALE.SALE_ID.eq(6L))
                .fetchOne();

        var r2 = ctx.selectFrom(BANK_TRANSACTION)
                .where(BANK_TRANSACTION.TRANSACTION_ID.eq(2L))
                .fetchOne();

        var r3 = ctx.selectFrom(SALE)
                .where(SALE.SALE_ID.eq(7L))
                .fetchOne();

        // There are two batches, one for r1 abd r3, and one r2
        // The order of records is not preserved (check the log).
        if (r1 != null && r2 != null && r3 != null) {
            int[] result = ctx.batchDelete(r1, r2, r3)
                    .execute();

            System.out.println("EXAMPLE 3: " + Arrays.toString(result));
        }
    }

    public void batchDeleteRecords3() {

        var r1 = ctx.selectFrom(SALE)
                .where(SALE.SALE_ID.eq(1L))
                .fetchOne();

        var r2 = ctx.selectFrom(BANK_TRANSACTION)
                .where(BANK_TRANSACTION.TRANSACTION_ID.eq(1L))
                .fetchOne();

        var r3 = ctx.selectFrom(PRODUCTLINEDETAIL)
                .where(PRODUCTLINEDETAIL.PRODUCT_LINE.eq("Classic Cars"))
                .fetchOne();

        // There are three batches, one for r1, one for r2, and one for r3 because the generated SQL with bind variables is not the same.
        // The order of records is preserved.
        if (r1 != null && r2 != null && r3 != null) {
            int[] result = ctx.batchDelete(r1, r2, r3)
                    .execute();

            System.out.println("EXAMPLE 4: " + Arrays.toString(result));
        }
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
                        .where(SALE.FISCAL_YEAR.eq((Integer) null)
                                .and(SALE.EMPLOYEE_NUMBER.eq((Long) null))
                                .and(SALE.SALE_.eq((Double) null)))
        );

        sales.forEach(s -> batch.bind(s.getFiscalYear(), s.getEmployeeNumber(), s.getSale()));
        batch.execute();
    }
}

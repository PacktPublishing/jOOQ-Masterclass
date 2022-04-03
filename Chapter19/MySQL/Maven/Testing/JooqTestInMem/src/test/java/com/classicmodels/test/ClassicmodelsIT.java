package com.classicmodels.test;

import java.math.BigDecimal;
import static jooq.generated.tables.Payment.PAYMENT;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Sale.SALE;
import jooq.generated.tables.records.PaymentRecord;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import org.jooq.DSLContext;
import org.jooq.Record2;
import org.jooq.Result;
import org.jooq.conf.MappedSchema;
import org.jooq.conf.RenderMapping;
import org.jooq.conf.RenderNameCase;
import org.jooq.impl.DSL;
import static org.jooq.impl.DSL.row;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

@JooqTest
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
public class ClassicmodelsIT {

    @Autowired
    private DSLContext ctx;

    @Autowired
    private TransactionTemplate template;

    @BeforeAll
    public void setup() {

        ctx.settings()
                // .withExecuteLogging(Boolean.FALSE)
                .withRenderNameCase(RenderNameCase.UPPER)
                .withRenderMapping(new RenderMapping()
                        .withSchemata(
                                new MappedSchema().withInput("classicmodels")
                                        .withOutput("PUBLIC")));
    }

    @Test
    public void givenSelectProductWhenFetchByIdThenResultOneRecord() {

        Result<Record2<Long, String>> result
                = ctx.select(PRODUCT.PRODUCT_ID, PRODUCT.PRODUCT_NAME)
                        .from(PRODUCT)
                        .where(PRODUCT.PRODUCT_ID.eq(1L))
                        .fetch();

        assertThat(result, hasSize(equalTo(1)));
        assertThat(result.getValue(0, PRODUCT.PRODUCT_ID), is(equalTo(1L)));
        assertThat(result.getValue(0, PRODUCT.PRODUCT_NAME), is(equalTo("1969 Harley Davidson Ultimate Chopper")));
    }

    @Test
    public void givenUpdateWhenCorrectThenAffected() {

        int affected = ctx.update(SALE).set(SALE.FISCAL_YEAR, 2004).where(SALE.FISCAL_YEAR.gt(2003))
                .execute();

        assertThat(affected, is(equalTo(17)));
    }

    @Test
    public void givenInsertWhenSameIdThenException() {

        Throwable ex = assertThrows(org.springframework.dao.DuplicateKeyException.class, () -> {

            ctx.insertInto(SALE, SALE.SALE_ID, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                    .values(1L, 2005, 1370L, 1282.64, 1, 15.55)
                    .execute();
        });

        assertThat(ex.getCause(), is(instanceOf(org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException.class)));
    }

    @Test
    public void givenBatchWhenCorrectThenAffected() {

        int[] affected = ctx.batch(
                ctx.insertInto(SALE, SALE.SALE_ID, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(100L, 2005, 1370L, 1282.64, 1, 15.55),
                ctx.insertInto(SALE, SALE.SALE_ID, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(101L, 2004, 1370L, 3938.24, 2, 22.33),
                ctx.insertInto(SALE, SALE.SALE_ID, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(102L, 2006, 1370L, 3923.24, 1, 12.55),
                ctx.insertInto(SALE, SALE.SALE_ID, SALE.FISCAL_YEAR, SALE.EMPLOYEE_NUMBER, SALE.SALE_, SALE.FISCAL_MONTH, SALE.REVENUE_GROWTH)
                        .values(103L, 2004, 1370L, 4676.14, 3, 10.2),
                ctx.delete(SALE).where(SALE.SALE_.lt(5000.0))
        ).execute();

        assertThat(affected.length, is(equalTo(5)));
        assertThat(affected[0], is(equalTo(1)));
        assertThat(affected[1], is(equalTo(1)));
        assertThat(affected[2], is(equalTo(1)));
        assertThat(affected[3], is(equalTo(1)));
        assertThat(affected[4], is(equalTo(19)));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void givenOptimisticLockingWhenDetectedThenException1() {

        // turn on jOOQ optimistic locking
        DSLContext derivedCtx = ctx.configuration().derive(
                ctx.settings()
                        .withExecuteWithOptimisticLocking(true)
                        .withExecuteWithOptimisticLockingExcludeUnversioned(true))
                .dsl();

        template.setPropagationBehavior(
                TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        Throwable ex = assertThrows(org.jooq.exception.DataAccessException.class, () -> {
            template.execute(new TransactionCallbackWithoutResult() {

                @Override
                protected void doInTransactionWithoutResult(
                        TransactionStatus status) {

                    PaymentRecord record = derivedCtx.fetchOne(PAYMENT,
                            row(PAYMENT.CUSTOMER_NUMBER, PAYMENT.CHECK_NUMBER)
                                    .eq(103L, "HQ336336"));

                    template.execute(new TransactionCallbackWithoutResult() {

                        @Override
                        protected void doInTransactionWithoutResult(
                                TransactionStatus status) {

                            PaymentRecord record = derivedCtx.fetchOne(PAYMENT,
                                    row(PAYMENT.CUSTOMER_NUMBER, PAYMENT.CHECK_NUMBER)
                                            .eq(103L, "HQ336336"));

                            record.setInvoiceAmount(BigDecimal.valueOf(0));

                            record.store();
                        }
                    });

                    record.setInvoiceAmount(BigDecimal.valueOf(1000));

                    record.store();
                }
            });
        });

        assertThat(ex.getMessage(), startsWith("Database record has been changed or doesn't exist any longer"));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void givenOptimisticLockingWhenDetectedThenException2() {

        // turn on jOOQ optimistic locking
        DSLContext derivedCtx = ctx.configuration().derive(
                ctx.settings()
                        .withExecuteWithOptimisticLocking(true)
                        .withExecuteWithOptimisticLockingExcludeUnversioned(true))
                .dsl();

        Throwable ex = assertThrows(org.jooq.exception.DataChangedException.class, () -> {

            derivedCtx.transaction(outer -> {
                PaymentRecord record1 = DSL.using(outer).fetchOne(PAYMENT,
                        row(PAYMENT.CUSTOMER_NUMBER, PAYMENT.CHECK_NUMBER)
                                .eq(103L, "HQ336336"));

                DSL.using(outer)
                        .transaction(inner -> {
                            PaymentRecord record2 = DSL.using(inner).fetchOne(PAYMENT,
                                    row(PAYMENT.CUSTOMER_NUMBER, PAYMENT.CHECK_NUMBER)
                                            .eq(103L, "HQ336336"));

                            record2.setInvoiceAmount(BigDecimal.valueOf(0));

                            record2.store();
                        });

                record1.setInvoiceAmount(BigDecimal.valueOf(1000));

                record1.store();
            });
        });

        assertThat(ex.getMessage(), startsWith("Database record has been changed or doesn't exist any longer"));
    }
}

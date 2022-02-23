package com.classicmodels.repository;

import com.classicmodels.exceptions.OptimisticLockingRetryFalied;
import java.math.BigDecimal;
import static jooq.generated.tables.Payment.PAYMENT;
import jooq.generated.tables.records.PaymentRecord;
import org.jooq.DSLContext;
import org.jooq.Result;
import static org.jooq.impl.DSL.row;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public Result<PaymentRecord> fetchAllPayment103() {

        return ctx.selectFrom(PAYMENT)
                .where(PAYMENT.CUSTOMER_NUMBER.eq(103L))
                .fetch();
    }

    public PaymentRecord fetchPayment(Long nr, String ch) {

        return ctx.selectFrom(PAYMENT)
                .where(row(PAYMENT.CUSTOMER_NUMBER, PAYMENT.CHECK_NUMBER)
                        .eq(row(nr, ch)))
                .fetchSingle();
    }

    @Transactional
    @Retryable(value = org.jooq.exception.DataChangedException.class,
            maxAttempts = 2, backoff = @Backoff(delay = 100))
    public int storePayment(PaymentRecord pr) {

        int stored = 0;

        try {
            ctx.attach(pr);
            stored = pr.store();
        } catch (org.jooq.exception.DataChangedException e) {

            BigDecimal invoiceAmount = pr.getInvoiceAmount();
            pr.refresh();

            if (invoiceAmount.doubleValue() > pr.getInvoiceAmount().doubleValue()) {
                pr.setInvoiceAmount(invoiceAmount);
                throw e;
            }

            throw new OptimisticLockingRetryFalied(e.getMessage());
        }

        return stored;
    }
}

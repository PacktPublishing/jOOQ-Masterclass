package com.classicmodels.repository;

import static jooq.generated.tables.Payment.PAYMENT;
import static jooq.generated.tables.Paymentdetail.PAYMENTDETAIL;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.row;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    // EXAMPLE 1
    public void joinPaymentPaymentdetailViaOn() {

        System.out.println("EXAMPLE 1\n"
                + ctx.select(PAYMENT.INVOICE_AMOUNT, PAYMENT.CACHING_DATE,
                        PAYMENTDETAIL.BANK_NAME, PAYMENTDETAIL.TRANSACTION_TYPE)
                        .from(PAYMENT)
                        .innerJoin(PAYMENTDETAIL)
                        .on(PAYMENT.CHECK_NUMBER.eq(PAYMENTDETAIL.CHECK_NUMBER)
                                .and(PAYMENT.CUSTOMER_NUMBER.eq(PAYMENTDETAIL.CUSTOMER_NUMBER)))
                        .fetch()
        );
    }

    // EXAMPLE 2
    public void joinPaymentPaymentdetailViaOnRow() {

        System.out.println("EXAMPLE 2\n"
                + ctx.select(PAYMENT.INVOICE_AMOUNT, PAYMENT.CACHING_DATE,
                        PAYMENTDETAIL.BANK_NAME, PAYMENTDETAIL.TRANSACTION_TYPE)
                        .from(PAYMENT)
                        .innerJoin(PAYMENTDETAIL)
                        .on(row(PAYMENT.CHECK_NUMBER, PAYMENT.CUSTOMER_NUMBER).eq(
                                row(PAYMENTDETAIL.CHECK_NUMBER, PAYMENTDETAIL.CUSTOMER_NUMBER)))
                        .fetch()
        );
    }

    // EXAMPLE 3
    public void joinPaymentPaymentdetailViaOnKey() {

        System.out.println("EXAMPLE 3\n"
                + ctx.select(PAYMENT.INVOICE_AMOUNT, PAYMENT.CACHING_DATE,
                        PAYMENTDETAIL.BANK_NAME, PAYMENTDETAIL.TRANSACTION_TYPE)
                        .from(PAYMENT)
                        .innerJoin(PAYMENTDETAIL)
                        .onKey()
                        .fetch()
        );
    }

    // EXAMPLE 4
    public void joinPaymentPaymentdetailViaOnKeyFK() {

        System.out.println("EXAMPLE 4\n"
                + ctx.select(PAYMENT.INVOICE_AMOUNT, PAYMENT.CACHING_DATE,
                        PAYMENTDETAIL.BANK_NAME, PAYMENTDETAIL.TRANSACTION_TYPE)
                        .from(PAYMENT)
                        .innerJoin(PAYMENTDETAIL)
                        .onKey(PAYMENTDETAIL.CHECK_NUMBER, PAYMENTDETAIL.CUSTOMER_NUMBER)
                        .fetch()
        );
    }
}
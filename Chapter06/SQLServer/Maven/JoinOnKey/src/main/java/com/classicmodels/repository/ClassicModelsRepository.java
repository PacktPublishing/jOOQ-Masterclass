package com.classicmodels.repository;

import static jooq.generated.Keys.PRODUCTLINEDETAIL_PRODUCTLINE_FK;
import static jooq.generated.tables.BankTransaction.BANK_TRANSACTION;
import static jooq.generated.tables.Payment.PAYMENT;
import static jooq.generated.tables.Productline.PRODUCTLINE;
import static jooq.generated.tables.Productlinedetail.PRODUCTLINEDETAIL;
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

    // EXAMPLE 1 (classic ON)
    public void joinPaymentBankTransactionViaOn() {

        System.out.println("EXAMPLE 1\n"
                + ctx.select(BANK_TRANSACTION.BANK_NAME, BANK_TRANSACTION.BANK_IBAN,
                        BANK_TRANSACTION.TRANSFER_AMOUNT, PAYMENT.INVOICE_AMOUNT)
                        .from(PAYMENT)
                        .innerJoin(BANK_TRANSACTION)
                        .on(PAYMENT.CUSTOMER_NUMBER.eq(BANK_TRANSACTION.CUSTOMER_NUMBER)
                                .and(PAYMENT.CHECK_NUMBER.eq(BANK_TRANSACTION.CHECK_NUMBER)))
                        .fetch()
        );
    }

    // EXAMPLE 2 (using row)
    public void joinPaymentBankTransactionViaOnRow() {

        System.out.println("EXAMPLE 2\n"
                + ctx.select(BANK_TRANSACTION.BANK_NAME, BANK_TRANSACTION.BANK_IBAN,
                        BANK_TRANSACTION.TRANSFER_AMOUNT, PAYMENT.INVOICE_AMOUNT)
                        .from(PAYMENT)
                        .innerJoin(BANK_TRANSACTION)
                        .on(row(PAYMENT.CUSTOMER_NUMBER, PAYMENT.CHECK_NUMBER)
                                .eq(row(BANK_TRANSACTION.CUSTOMER_NUMBER, BANK_TRANSACTION.CHECK_NUMBER)))
                        .fetch()
        );
    }

    // EXAMPLE 3 (using jOOQ onKey())
    public void joinPaymentBankTransactionViaOnKey() {

        System.out.println("EXAMPLE 3\n"
                + ctx.select(BANK_TRANSACTION.BANK_NAME, BANK_TRANSACTION.BANK_IBAN,
                        BANK_TRANSACTION.TRANSFER_AMOUNT, PAYMENT.INVOICE_AMOUNT)
                        .from(PAYMENT)
                        .innerJoin(BANK_TRANSACTION)
                        .onKey()
                        .fetch()
        );
    }

    // EXAMPLE 4
    public void joinProductlineProductlinedetailViaOnKeyTF1() {

        System.out.println("EXAMPLE 4\n"
                + ctx.select(PRODUCTLINE.TEXT_DESCRIPTION, PRODUCTLINE.CREATED_ON,
                        PRODUCTLINEDETAIL.LINE_CAPACITY, PRODUCTLINEDETAIL.LINE_TYPE)
                        .from(PRODUCTLINE)
                        .innerJoin(PRODUCTLINEDETAIL)
                        .onKey(PRODUCTLINEDETAIL.PRODUCT_LINE, PRODUCTLINEDETAIL.CODE)                        
                        .fetch()
        );
    }

    // EXAMPLE 5
    public void joinProductlineProductlinedetailViaOnKeyFK() {

        System.out.println("EXAMPLE 5\n"
                + ctx.select(PRODUCTLINE.TEXT_DESCRIPTION, PRODUCTLINE.CREATED_ON,
                        PRODUCTLINEDETAIL.LINE_CAPACITY, PRODUCTLINEDETAIL.LINE_TYPE)
                        .from(PRODUCTLINE)
                        .innerJoin(PRODUCTLINEDETAIL)
                        .onKey(PRODUCTLINEDETAIL_PRODUCTLINE_FK)
                        .fetch()
        );
    }

    // EXAMPLE 6
    public void joinProductlineProductlinedetailViaOnKeyTF2() {

        System.out.println("EXAMPLE 6.1\n"
                + ctx.select(PRODUCTLINE.TEXT_DESCRIPTION, PRODUCTLINE.CREATED_ON,
                        PRODUCTLINEDETAIL.LINE_CAPACITY, PRODUCTLINEDETAIL.LINE_TYPE)
                        .from(PRODUCTLINE)
                        .innerJoin(PRODUCTLINEDETAIL)
                        .onKey(PRODUCTLINEDETAIL.PRODUCT_LINE) // PRODUCTLINEDETAIL.CODE is auto-added
                        .fetch()
        );
        
        System.out.println("EXAMPLE 6.2\n"
                + ctx.select(PRODUCTLINE.TEXT_DESCRIPTION, PRODUCTLINE.CREATED_ON,
                        PRODUCTLINEDETAIL.LINE_CAPACITY, PRODUCTLINEDETAIL.LINE_TYPE)
                        .from(PRODUCTLINE)
                        .innerJoin(PRODUCTLINEDETAIL)
                        .onKey(PRODUCTLINEDETAIL.CODE) // PRODUCTLINEDETAIL.PRODUCT_LINE is auto-added
                        .fetch()
        );
    }
}
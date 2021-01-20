package com.classicmodels.repository;

import static jooq.generated.tables.BankTransaction.BANK_TRANSACTION;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Payment.PAYMENT;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Productline.PRODUCTLINE;
import org.jooq.DSLContext;
import org.jooq.JSON;
import org.jooq.Record1;
import org.jooq.Result;
import static org.jooq.impl.DSL.lateral;
import static org.jooq.impl.DSL.select;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void jsonProductlineProductOrderdetail() {              
        
        /*
        var result = ctx.select(
                PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION,
                select(PRODUCT.PRODUCT_LINE, PRODUCT.PRODUCT_NAME,
                        select(ORDERDETAIL.PRODUCT_ID, ORDERDETAIL.PRICE_EACH)
                                .from(ORDERDETAIL).where(ORDERDETAIL.PRODUCT_ID.eq(PRODUCT.PRODUCT_ID))
                                .forJSON().path().asField("vvvv"))
                        .from(PRODUCT)
                        .where(PRODUCT.PRODUCT_LINE.eq(PRODUCTLINE.PRODUCT_LINE))
                        .forJSON().path().asField("tttt"))              
                .from(PRODUCTLINE).limit(5)
                .forJSON().path()
                .fetch();

        System.out.println("Example 1:\n" + result.formatJSON());
        */
    }

    /*
        Result<Record1<JSON>> result1 = ctx.select(
                PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION,
                PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR, PRODUCT.QUANTITY_IN_STOCK,
                ORDERDETAIL.QUANTITY_ORDERED, ORDERDETAIL.PRICE_EACH)
                .from(PRODUCTLINE)
                .join(PRODUCT)
                .on(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                .join(ORDERDETAIL)
                .on(PRODUCT.PRODUCT_ID.eq(ORDERDETAIL.PRODUCT_ID))
                .limit(5)
                .forJSON().path().root("data")
                .fetch();
        System.out.println("Example 1.1:\n" + result1.formatJSON());

        Result<Record1<JSON>> result2 = ctx.select(
                PRODUCTLINE.PRODUCT_LINE,
                PRODUCTLINE.TEXT_DESCRIPTION,
                PRODUCT.PRODUCT_NAME.as("product.product_name"),
                PRODUCT.PRODUCT_VENDOR.as("product.product_vendor"),
                PRODUCT.QUANTITY_IN_STOCK.as("product.quantity_in_stock"),
                ORDERDETAIL.QUANTITY_ORDERED.as("product.orderdetail.quantity_order"),
                ORDERDETAIL.PRICE_EACH.as("product.orderdetail.price_each"))
                .from(PRODUCTLINE)
                .join(PRODUCT)
                .on(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                .join(ORDERDETAIL)
                .on(PRODUCT.PRODUCT_ID.eq(ORDERDETAIL.PRODUCT_ID))
                .limit(5)
                .forJSON().path().root("data")
                .fetch();
        System.out.println("Example 1.2:\n" + result2.formatJSON());

        Result<Record1<JSON>> result3 = ctx.select(
                PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.TEXT_DESCRIPTION,
                PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_VENDOR, PRODUCT.QUANTITY_IN_STOCK,
                ORDERDETAIL.QUANTITY_ORDERED, ORDERDETAIL.PRICE_EACH)
                .from(PRODUCTLINE)
                .join(PRODUCT)
                .on(PRODUCTLINE.PRODUCT_LINE.eq(PRODUCT.PRODUCT_LINE))
                .join(ORDERDETAIL)
                .on(PRODUCT.PRODUCT_ID.eq(ORDERDETAIL.PRODUCT_ID))
                .forJSON().auto().root("data")
                .fetch();
        System.out.println("Example 1.3:\n" + result3.formatJSON());
    }

    public void jsonCustomerPaymentBankTransactionCustomerdetail() {
/*
        Result<Record1<JSON>> result = ctx.select(CUSTOMER.CUSTOMER_NAME,
                CUSTOMER.CREDIT_LIMIT, lateral(
                 select(PAYMENT.CUSTOMER_NUMBER,
                        PAYMENT.INVOICE_AMOUNT, PAYMENT.CACHING_DATE,
                         lateral(select(BANK_TRANSACTION.BANK_NAME, BANK_TRANSACTION.TRANSFER_AMOUNT)
                                .from(BANK_TRANSACTION)
                                .where(BANK_TRANSACTION.CUSTOMER_NUMBER.eq(PAYMENT.CUSTOMER_NUMBER)
                                        .and(BANK_TRANSACTION.CHECK_NUMBER.eq(PAYMENT.CHECK_NUMBER)))))
                        .from(PAYMENT)
                        .where(PAYMENT.CUSTOMER_NUMBER.eq(CUSTOMER.CUSTOMER_NUMBER))))
                .from(CUSTOMERDETAIL)
                .where(CUSTOMERDETAIL.CUSTOMER_NUMBER.eq(CUSTOMER.CUSTOMER_NUMBER))
                .fetch();
        System.out.println("Example 2:\n" + result.formatJSON());
     */
}

package com.classicmodels.repository;

import java.math.BigDecimal;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Payment.PAYMENT;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.nvl;
import static org.jooq.impl.DSL.selectDistinct;
import static org.jooq.impl.DSL.sum;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    /* Oracle's partitioned OUTER JOIN */
    // EXAMPLE 1 - identify gaps in the series of dates
    /*
    select 
      "PAYMENT"."PAYMENT_DATE", 
      "CUSTOMER"."CUSTOMER_NAME", 
      sum("PAYMENT"."INVOICE_AMOUNT") 
    from 
      "CUSTOMER" 
      join "PAYMENT" on "CUSTOMER"."CUSTOMER_NUMBER" = "PAYMENT"."CUSTOMER_NUMBER" 
    group by 
      "PAYMENT"."PAYMENT_DATE", 
      "CUSTOMER"."CUSTOMER_NAME" 
    order by 1, 2
     */
    public void joinCustomerPaymentIdentifyDataGaps() {

        System.out.println("EXAMPLE 1\n"
                + ctx.select(PAYMENT.PAYMENT_DATE, CUSTOMER.CUSTOMER_NAME, sum(PAYMENT.INVOICE_AMOUNT))
                        .from(CUSTOMER)
                        .innerJoin(PAYMENT)
                        .on(CUSTOMER.CUSTOMER_NUMBER.eq(PAYMENT.CUSTOMER_NUMBER))
                        .groupBy(PAYMENT.PAYMENT_DATE, CUSTOMER.CUSTOMER_NAME)
                        .orderBy(1, 2)
                        .fetch()
        );
    }

    // EXAMPLE 2 - produce a summary showing total invoices of each customer per day
    /*
    select 
      "pdate", 
      "CUSTOMER"."CUSTOMER_NAME", 
      sum(nvl("PAYMENT"."INVOICE_AMOUNT", ?)) 
    from 
      "CUSTOMER" 
    cross join (
        select 
          distinct "PAYMENT"."PAYMENT_DATE" "pdate" 
        from 
          "PAYMENT"
      ) "alias_120640537" 
      left outer join "PAYMENT" on (
        "CUSTOMER"."CUSTOMER_NUMBER" = "PAYMENT"."CUSTOMER_NUMBER" 
        and "pdate" = "PAYMENT"."PAYMENT_DATE"
      ) 
    group by 
      "pdate", 
      "CUSTOMER"."CUSTOMER_NAME" 
    order by 1, 2    
     */
    public void joinCustomerPaymentFillGaps() {

        System.out.println("EXAMPLE 2\n"
                + ctx.select(field(name("pdate")), CUSTOMER.CUSTOMER_NAME, sum(nvl(PAYMENT.INVOICE_AMOUNT, BigDecimal.ZERO)))
                        .from(CUSTOMER)
                        .crossJoin(selectDistinct(PAYMENT.PAYMENT_DATE.as(name("pdate"))).from(PAYMENT))
                        .leftJoin(PAYMENT)
                        .on(CUSTOMER.CUSTOMER_NUMBER.eq(PAYMENT.CUSTOMER_NUMBER)
                                .and(field(name("pdate")).eq(PAYMENT.PAYMENT_DATE)))
                        .groupBy(field(name("pdate")), CUSTOMER.CUSTOMER_NAME)
                        .orderBy(1, 2)
                        .fetch()
        );
    }

    // EXAMPLE 3 - produce a summary showing total invoices of each customer per day in Oracle style
    /*
    select 
      "PAYMENT"."PAYMENT_DATE", 
      "CUSTOMER"."CUSTOMER_NAME", 
      sum(nvl("PAYMENT"."INVOICE_AMOUNT", ?)) 
    from 
      "CUSTOMER" 
      left outer join "PAYMENT" partition by ("PAYMENT"."PAYMENT_DATE") 
        on "CUSTOMER"."CUSTOMER_NUMBER" = "PAYMENT"."CUSTOMER_NUMBER" 
    group by 
      "PAYMENT"."PAYMENT_DATE", 
      "CUSTOMER"."CUSTOMER_NAME" 
    order by 1, 2    
    */
    public void joinCustomerPaymentFillGapsOracleStyle() {

        System.out.println("EXAMPLE 3\n"
                + ctx.select(PAYMENT.PAYMENT_DATE, CUSTOMER.CUSTOMER_NAME,
                        sum(nvl(PAYMENT.INVOICE_AMOUNT, BigDecimal.ZERO)))
                        .from(CUSTOMER)
                        .leftOuterJoin(PAYMENT)
                        .partitionBy(PAYMENT.PAYMENT_DATE)
                        .on(CUSTOMER.CUSTOMER_NUMBER.eq(PAYMENT.CUSTOMER_NUMBER))
                        .groupBy(PAYMENT.PAYMENT_DATE, CUSTOMER.CUSTOMER_NAME)
                        .orderBy(1, 2)
                        .fetch()
        );
    }
}
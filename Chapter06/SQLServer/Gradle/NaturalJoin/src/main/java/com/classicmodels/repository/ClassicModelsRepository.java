package com.classicmodels.repository;

import static jooq.generated.tables.BankTransaction.BANK_TRANSACTION;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Customerdetail.CUSTOMERDETAIL;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Payment.PAYMENT;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
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

    // EXAMPLE 1
    public void naturalJoinEmployeeSale() {

        System.out.println("EXAMPLE 1\n"
                + ctx.select()
                        .from(EMPLOYEE).naturalJoin(SALE)
                        .fetch()
        );
    }

    // EXAMPLE 2
    public void naturalLeftOuterJoinEmployeeSale() {

        System.out.println("EXAMPLE 2.1\n"
                + ctx.select()
                        .from(EMPLOYEE.naturalLeftOuterJoin(SALE))
                        .fetch()
        );

        System.out.println("EXAMPLE 2.2 (EXCLUSIVE) \n"
                + ctx.select()
                        .from(EMPLOYEE.naturalLeftOuterJoin(SALE))
                        .where(SALE.EMPLOYEE_NUMBER.isNull())
                        .fetch()
        );
    }

    // EXAMPLE 3
    public void naturalRightOuterJoinEmployeeSale() {

        System.out.println("EXAMPLE 3.1\n"
                + ctx.select()
                        .from(EMPLOYEE.naturalRightOuterJoin(SALE))
                        .fetch()
        );

        System.out.println("EXAMPLE 3.2 (EXCLUSIVE) \n"
                + ctx.select()
                        .from(EMPLOYEE.naturalRightOuterJoin(SALE))
                        .where(EMPLOYEE.EMPLOYEE_NUMBER.isNull())
                        .fetch()
        );
    }

    // EXAMPLE 4
    public void naturalFullOuterJoinEmployeeSale() {

        System.out.println("EXAMPLE 4.1\n"
                + ctx.select()
                        .from(EMPLOYEE.naturalFullOuterJoin(SALE))
                        .fetch()
        );

        System.out.println("EXAMPLE 4.2 (EXCLUSIVE) \n"
                + ctx.select()
                        .from(EMPLOYEE.naturalFullOuterJoin(SALE))
                        .where(EMPLOYEE.EMPLOYEE_NUMBER.isNull()
                                .or(SALE.EMPLOYEE_NUMBER.isNull()))
                        .fetch()
        );
        
        // https://www.reddit.com/r/PostgreSQL/comments/lotcxf/is_there_such_a_thing_that_can_join_two/
        System.out.println("EXAMPLE 4.3:\n"
                + ctx.select().from(select().from(CUSTOMER).asTable("t1")
                        .naturalFullOuterJoin(select().from(CUSTOMERDETAIL).asTable("t2")))
                        .fetch()
        );
    }

    // EXAMPLE 5
    public void naturalJoinOrderCustomerPayment() {

        System.out.println("EXAMPLE 5\n"
                + ctx.select()
                        .from(ORDER.naturalJoin(CUSTOMER).naturalJoin(PAYMENT))
                        .fetch()
        );
    }

    // EXAMPLE 6
    public void naturalJoinOfficeCustomerdetail() {

        System.out.println("EXAMPLE 6\n"
                + ctx.select()
                        .from(OFFICE.naturalJoin(CUSTOMERDETAIL))
                        .fetch()
        );
    }

    // EXAMPLE 7
    public void naturalJoinPaymentBankTransaction() {

        // NATURAL JOIN will not produce the expected output because
        // it will use on join the CACHING_DATE column as well since this
        // column is present in both tables
        System.out.println("EXAMPLE 7.1\n"
                + ctx.select()
                        .from(PAYMENT.naturalJoin(BANK_TRANSACTION))
                        .fetch()
        );

        // In such case, we have to rely on explicit ON to eliminate
        // CACHING_DATE column from JOIN. 
        System.out.println("EXAMPLE 7.2\n"
                + ctx.select()
                        .from(PAYMENT.innerJoin(BANK_TRANSACTION)
                                .on(PAYMENT.CUSTOMER_NUMBER.eq(BANK_TRANSACTION.CUSTOMER_NUMBER)
                                        .and(PAYMENT.CHECK_NUMBER.eq(BANK_TRANSACTION.CHECK_NUMBER))))
                        .fetch()
        );

        // Or, less verbose via jOOQ, onKey() 
        System.out.println("EXAMPLE 7.3\n"
                + ctx.select()
                        .from(PAYMENT.innerJoin(BANK_TRANSACTION)
                                .onKey())
                        .fetch()
        );

        // But, if we actually want to join only on CACHING_DATE
        // then use ON or USING
        System.out.println("EXAMPLE 7.4\n"
                + ctx.select()
                        .from(PAYMENT.innerJoin(BANK_TRANSACTION)
                                .using(PAYMENT.CACHING_DATE))
                        //.on(PAYMENT.CACHING_DATE.eq(BANK_TRANSACTION.CACHING_DATE)))
                        .fetch()
        );
    }
}
package com.classicmodels.repository;

import static jooq.generated.tables.BankTransaction.BANK_TRANSACTION;
import static jooq.generated.tables.Payment.PAYMENT;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.table;
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
                        .from(table("EMPLOYEE")).naturalJoin(table("SALE"))
                        .fetch()
        );
    }

    // EXAMPLE 2
    public void naturalLeftOuterJoinEmployeeSale() {

        System.out.println("EXAMPLE 2\n"
                + ctx.select()
                        .from(table("EMPLOYEE")).naturalLeftOuterJoin(table("SALE"))
                        .fetch()
        );
    }

    // EXAMPLE 3
    public void naturalRightOuterJoinEmployeeSale() {

        System.out.println("EXAMPLE 3\n"
                + ctx.select()
                        .from(table("EMPLOYEE")).naturalRightOuterJoin(table("SALE"))
                        .fetch()
        );
    }

    // EXAMPLE 4
    public void naturalFullOuterJoinEmployeeSale() {

        System.out.println("EXAMPLE 4\n"
                + ctx.select()
                        .from(table("EMPLOYEE")).naturalFullOuterJoin(table("SALE"))
                        .fetch()
        );    
    }

    // EXAMPLE 5
    public void naturalJoinOrderCustomerPayment() {
        
        System.out.println("EXAMPLE 5\n"
                + ctx.select()
                        .from(table(name("ORDER")).naturalJoin(table("CUSTOMER")).naturalJoin(table("PAYMENT")))
                        .fetch()
        );
    }

    // EXAMPLE 6
    public void naturalJoinOfficeCustomerdetail() {
        
        System.out.println("EXAMPLE 6\n"
                + ctx.select()
                        .from(table("OFFICE").naturalJoin(table("CUSTOMERDETAIL")))
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
                        .from(table("PAYMENT").naturalJoin(table("BANK_TRANSACTION")))
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
        
        // But, if we actually want to join only on CACHING_DATE then use ON
        System.out.println("EXAMPLE 7.4\n"
                + ctx.select()
                        .from(PAYMENT.innerJoin(BANK_TRANSACTION)                                
                                .on(PAYMENT.CACHING_DATE.eq(BANK_TRANSACTION.CACHING_DATE)))
                        .fetch()
        );
    }    
}
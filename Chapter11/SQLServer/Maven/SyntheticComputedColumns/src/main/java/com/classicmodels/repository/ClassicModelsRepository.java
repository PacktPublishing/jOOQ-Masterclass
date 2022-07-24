package com.classicmodels.repository;

import static jooq.generated.tables.BankTransaction.BANK_TRANSACTION;
import org.jooq.DSLContext;
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

    @Transactional
    public void remainingToTransfer() {       
        
        System.out.println(
                ctx.select(
                        BANK_TRANSACTION.CUSTOMER_NUMBER, 
                        BANK_TRANSACTION.CHECK_NUMBER,
                        sum(BANK_TRANSACTION.TRANSFER_AMOUNT).as("total_transfer_amount"), 
                        BANK_TRANSACTION.payment().INVOICE_AMOUNT,
                        BANK_TRANSACTION.REFUND_AMOUNT)
                        .from(BANK_TRANSACTION)
                        .groupBy(BANK_TRANSACTION.CUSTOMER_NUMBER, 
                                BANK_TRANSACTION.CHECK_NUMBER,                                                               
                                BANK_TRANSACTION.payment().INVOICE_AMOUNT)
                        .orderBy(BANK_TRANSACTION.CUSTOMER_NUMBER)                        
                        .fetch()
        );
    }
}

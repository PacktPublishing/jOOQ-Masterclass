package com.classicmodels.repository;

import static jooq.generated.tables.Payment.PAYMENT;
import jooq.generated.tables.records.PaymentRecord;
import org.jooq.DSLContext;
import org.jooq.Result;
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

    @Transactional
    public int mergePayment(PaymentRecord pr) {
        
        ctx.attach(pr);        
        // or, pr.attach(ctx.configuration());
        
        return pr.merge();
    }
}

package com.classicmodels.repository;

import java.util.List;
import static jooq.generated.tables.Payment.PAYMENT;
import jooq.generated.tables.daos.PaymentRepository;
import jooq.generated.tables.pojos.Payment;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ClassicModelsRepository {

    private final DSLContext ctx;
    private final PaymentRepository paymentRepository;

    public ClassicModelsRepository(DSLContext ctx, PaymentRepository paymentRepository) {
        this.ctx = ctx;
        this.paymentRepository = paymentRepository;
    }
  
    public List<Payment> fetchAllPayment103() {

        return ctx.selectFrom(PAYMENT)
                .where(PAYMENT.CUSTOMER_NUMBER.eq(103L))
                .fetchInto(Payment.class);
    }   

    @Transactional
    public void mergePayment(Payment p) {
        
        paymentRepository.merge(p);
    }
}

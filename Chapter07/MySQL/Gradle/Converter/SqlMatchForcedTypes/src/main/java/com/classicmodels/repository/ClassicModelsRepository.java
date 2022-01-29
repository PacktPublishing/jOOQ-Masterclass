package com.classicmodels.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import static jooq.generated.tables.Payment.PAYMENT;
import org.jooq.DSLContext;
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
    public void insertPayment() throws JsonProcessingException {

        JsonNode paymentDate = new ObjectMapper().readTree(
                "{\"Timestamp\":\"2004-10-19 12:00:01\"}");

        JsonNode cachingDate = new ObjectMapper().readTree(
                "{\"Timestamp\":\"2004-10-20 13:42:41\"}");

        ctx.insertInto(PAYMENT, PAYMENT.CUSTOMER_NUMBER, PAYMENT.CHECK_NUMBER,
                PAYMENT.PAYMENT_DATE, PAYMENT.INVOICE_AMOUNT, PAYMENT.CACHING_DATE)
                .values(103L, UUID.randomUUID().toString(), 
                        paymentDate, BigDecimal.valueOf(18293.42), cachingDate)
                .execute();
    }

    public void fetchPayment() {

        List<JsonNode> paymentDate = ctx.select(PAYMENT.PAYMENT_DATE)
                .from(PAYMENT)
                .fetch(PAYMENT.PAYMENT_DATE);

        System.out.println("Payment dates: " + paymentDate);
    }
}

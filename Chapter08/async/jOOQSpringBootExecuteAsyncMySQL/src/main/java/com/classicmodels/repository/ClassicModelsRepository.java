package com.classicmodels.repository;

import java.util.concurrent.CompletableFuture;
import static jooq.generated.tables.BankTransaction.BANK_TRANSACTION;
import static jooq.generated.tables.Customer.CUSTOMER;
import org.jooq.DSLContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    @Async
    public CompletableFuture<Integer> updateCreditLimitAsync() {

        return ctx.update(CUSTOMER)
                .set(CUSTOMER.CREDIT_LIMIT, CUSTOMER.CREDIT_LIMIT.minus(10))
                .executeAsync().toCompletableFuture();
    }

    @Async
    public CompletableFuture<Integer> deleteFailedTransactionsAsync() {

        return ctx.deleteFrom(BANK_TRANSACTION)
                .where(BANK_TRANSACTION.STATUS.eq("FAILED"))
                .executeAsync().toCompletableFuture();
    }
}
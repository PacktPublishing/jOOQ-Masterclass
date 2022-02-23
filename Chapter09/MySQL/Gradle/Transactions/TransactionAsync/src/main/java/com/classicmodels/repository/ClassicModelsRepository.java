package com.classicmodels.repository;

import java.util.concurrent.CompletableFuture;
import static jooq.generated.tables.Sale.SALE;
import static jooq.generated.tables.Token.TOKEN;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    // this transaction commits
    @Async
    public CompletableFuture<Integer> executeFirstJOOQTransaction() {

        return ctx.transactionResultAsync(configuration -> {

            int result = 0;

            result += DSL.using(configuration).insertInto(TOKEN)
                    .set(TOKEN.SALE_ID, 1L)
                    .set(TOKEN.AMOUNT, 500d)
                    .execute();

            result += DSL.using(configuration).insertInto(TOKEN)
                    .set(TOKEN.SALE_ID, 1L)
                    .set(TOKEN.AMOUNT, 1000d)
                    .execute();

            return result;

        }).toCompletableFuture();
    }

    // this transaction is roll backed
    @Async
    public CompletableFuture<Integer> executeSecondJOOQTransaction() {

        return ctx.transactionResultAsync(configuration -> {

            int result = 0;

            result += DSL.using(configuration).delete(SALE)
                    .where(SALE.SALE_ID.eq(2L))
                    .execute();

            result += DSL.using(configuration).insertInto(TOKEN)
                    .set(TOKEN.SALE_ID, 2L)                    
                    .set(TOKEN.AMOUNT, 1000d)
                    .execute();

            return result;

        }).toCompletableFuture();
    }
}

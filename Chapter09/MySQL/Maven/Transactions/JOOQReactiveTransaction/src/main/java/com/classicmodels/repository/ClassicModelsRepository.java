package com.classicmodels.repository;

import static jooq.generated.tables.Sale.SALE;
import static jooq.generated.tables.Token.TOKEN;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void rollbackJOOQTransaction() {

        Flux<?> flux = Flux.from(ctx.transactionPublisher(outer -> Flux
                .from(
                        DSL.using(outer).delete(SALE) // or, outer.dsl()
                                .where(SALE.SALE_ID.eq(2L)))
                .thenMany(
                        DSL.using(outer).insertInto(TOKEN) // or, outer.dsl()
                                .set(TOKEN.SALE_ID, 1L)
                                .set(TOKEN.AMOUNT, 1000d))));

        flux.subscribe();

        // at this moment transaction should commit, but the error caused by                 
        // the previous INSERT will lead to a rollback
    }

    public void dontRollbackJOOQTransaction() {

        Flux<?> flux = Flux.from(ctx.transactionPublisher(outer -> Flux
                .from(
                        DSL.using(outer).delete(SALE) // or, outer.dsl()
                                .where(SALE.SALE_ID.eq(1L)))
                .thenMany(
                        DSL.using(outer).insertInto(TOKEN) // or, outer.dsl()
                                .set(TOKEN.SALE_ID, 1L)
                                .set(TOKEN.AMOUNT, 1000d))
                .onErrorReturn(1)
        ));

        flux.subscribe();
    }

    public void nestedJOOQTransaction() {

        Flux<?> flux = Flux.from(
                ctx.transactionPublisher(outer
                        -> Flux.from(DSL.using(outer).delete(SALE) // or, outer.dsl()
                        .where(SALE.SALE_ID.eq(2L)))
                        .thenMany(
                                Flux.from(DSL.using(outer).transactionPublisher( // or, outer.dsl()
                                        inner -> Flux.from(
                                                DSL.using(inner).insertInto(TOKEN) // or, inner.dsl()
                                                        .set(TOKEN.SALE_ID, 1L)
                                                        .set(TOKEN.AMOUNT, 1000d)
                                        )))
                        )));

        flux.subscribe();
    }

    public void nestedDontRollbackOuterJOOQTransaction() {

        Flux<?> flux = Flux.from(
                ctx.transactionPublisher(outer
                        -> Flux.from(DSL.using(outer).delete(SALE) // or, outer.dsl()
                        .where(SALE.SALE_ID.eq(1L)))
                        .thenMany(
                                Flux.from(DSL.using(outer).transactionPublisher( // or, outer.dsl()
                                        inner -> Flux.from(
                                                DSL.using(inner).insertInto(TOKEN) // or, inner.dsl()
                                                        .set(TOKEN.SALE_ID, 1L)
                                                        .set(TOKEN.AMOUNT, 1000d)
                                        ).doOnError(ex -> {
                                            System.out.println("Exception caught: " + ex);
                                            throw new RuntimeException(ex.getMessage(), ex);
                                        })))
                        )
                        .onErrorReturn(1)
                ));

        flux.subscribe();
    }
}

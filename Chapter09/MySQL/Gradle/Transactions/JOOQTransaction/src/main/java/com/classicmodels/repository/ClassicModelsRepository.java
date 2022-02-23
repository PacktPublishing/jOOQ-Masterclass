package com.classicmodels.repository;

import static jooq.generated.tables.Sale.SALE;
import static jooq.generated.tables.Token.TOKEN;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;        
    }
    
    public void rollbackJOOQTransaction() {

        ctx.transaction(configuration -> {
            
                DSL.using(configuration).delete(SALE) // or, configuration.dsl()
                        .where(SALE.SALE_ID.eq(1L))
                        .execute();

                DSL.using(configuration).insertInto(TOKEN)                    
                        .set(TOKEN.SALE_ID, 1L)
                        .set(TOKEN.AMOUNT, 1000d)
                        .execute();

                // at this moment transaction should commit, but the error caused by                 
                // the previous INSERT will lead to a rollback
        });
    }

    public void dontRollbackJOOQTransaction() {

        ctx.transaction(configuration -> {

            try {
                DSL.using(configuration).delete(SALE) // or, configuration.dsl()
                        .where(SALE.SALE_ID.eq(1L))
                        .execute();

                DSL.using(configuration).insertInto(TOKEN)                        
                        .set(TOKEN.SALE_ID, 1L)
                        .set(TOKEN.AMOUNT, 1000d)
                        .execute();
            } catch (RuntimeException e) {
                System.out.println("I've decided that this error doesn't require rollback ...");
            }
        });
    }

    public void nestedJOOQTransaction() {

        ctx.transaction(outer -> { 

            DSL.using(outer).delete(SALE) // or, outer.dsl()
                    .where(SALE.SALE_ID.eq(2L))
                    .execute();

            // savepoint created
            DSL.using(outer)
                    .transaction(inner -> {
                        DSL.using(inner).insertInto(TOKEN) // or, inner.dsl()
                                .set(TOKEN.SALE_ID, 1L)                                
                                .set(TOKEN.AMOUNT, 1000d)
                                .execute();
                    });
        });
    }

    public void nestedDontRollbackOuterJOOQTransaction() {

        ctx.transaction(outer -> {
            try {
                DSL.using(outer).delete(SALE)
                        .where(SALE.SALE_ID.eq(1L))
                        .execute();

                // savepoint created
                try {
                    DSL.using(outer)
                            .transaction(inner -> {
                                DSL.using(inner).insertInto(TOKEN)
                                        .set(TOKEN.SALE_ID, 1L)                                        
                                        .set(TOKEN.AMOUNT, 1000d)
                                        .execute();
                            });
                } catch (RuntimeException e) {
                    throw e;
                }
                
            } catch (RuntimeException e) {
                System.out.println("I've decided that this error doesn't require rollback of the outer transaction ...");
                // throw e; // rollback
            }
        });
    }
}

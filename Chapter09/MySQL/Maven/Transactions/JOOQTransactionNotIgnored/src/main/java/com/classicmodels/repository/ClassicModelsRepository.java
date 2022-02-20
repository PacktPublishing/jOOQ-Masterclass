package com.classicmodels.repository;

import static jooq.generated.tables.Token.TOKEN;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public void doSomethingAndExecuteSpringTransaction() {

        System.out.println("Calling executeSpringTransaction() ...");
        executeSpringTransaction();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)     // this is ignored!
    private void executeSpringTransaction() { // public doesn't solve the problem!

        ctx.insertInto(TOKEN)
                .set(TOKEN.SALE_ID, 1L)
                .set(TOKEN.AMOUNT, 500d)
                .execute();

        ctx.insertInto(TOKEN)
                .set(TOKEN.SALE_ID, 1L)
                .set(TOKEN.AMOUNT, 1000d)
                .execute();
    }

    public void doSomethingAndExecuteJOOQTransaction() {

        System.out.println("Calling executeJOOQTransaction() ...");
        executeJOOQTransaction();
    }

    private void executeJOOQTransaction() { // public works as well

        ctx.transaction(configuration -> {

            DSL.using(configuration).insertInto(TOKEN)
                    .set(TOKEN.SALE_ID, 1L)
                    .set(TOKEN.AMOUNT, 500d)
                    .execute();

            DSL.using(configuration).insertInto(TOKEN)
                    .set(TOKEN.SALE_ID, 1L)
                    .set(TOKEN.AMOUNT, 1000d)
                    .execute();
        });
    }
}

package com.classicmodels.repository;

import com.classicmodels.providers.MyTransactionProvider;
import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;

@Repository
public class ClassicModelsRepository {

    private final DSLContext ctx;
    private final PlatformTransactionManager txManager;

    public ClassicModelsRepository(DSLContext ctx, PlatformTransactionManager txManager) {
        this.ctx = ctx;
        this.txManager = txManager;

        // affects the currently injected DSLContext
        ctx.configuration().set(new MyTransactionProvider(txManager));

        //or use a derived DSLContext
        // DSLContext ctxDerived = ctx.configuration().derive(new MyTransactionProvider(txManager)).dsl();
    }

    public void sample() {

        // Uses a new transaction with name [TRANSACTION_1000]: PROPAGATION_REQUIRES_NEW,
        //                                   ISOLATION_READ_COMMITTED,timeout_1,readOnly
        ctx.transaction(configuration -> {
            DSL.using(configuration).
                    selectFrom(PRODUCT).fetch();
        });
    }
}

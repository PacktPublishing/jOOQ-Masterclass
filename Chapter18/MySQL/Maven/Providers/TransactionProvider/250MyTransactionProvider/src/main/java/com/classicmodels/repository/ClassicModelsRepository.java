package com.classicmodels.repository;

import static jooq.generated.tables.Product.PRODUCT;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {

    private final DSLContext ctx;
  
    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;    
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

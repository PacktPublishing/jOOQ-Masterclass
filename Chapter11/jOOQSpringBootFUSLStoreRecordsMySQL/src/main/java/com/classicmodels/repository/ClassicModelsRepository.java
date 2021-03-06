package com.classicmodels.repository;

import static jooq.generated.tables.Product.PRODUCT;
import jooq.generated.tables.records.ProductRecord;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

@Repository
public class ClassicModelsRepository {

    private final DSLContext ctx;
    private final TransactionTemplate template;

    public ClassicModelsRepository(DSLContext ctx, TransactionTemplate template) {
        this.ctx = ctx;
        this.template = template;
    }

    public void fetchProductsViaTwoTransactions() {
        template.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        template.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {

                Result<ProductRecord> products1 = ctx.selectFrom(PRODUCT)
                        .orderBy(PRODUCT.PRODUCT_ID)
                        .limit(3)
                        .forUpdate()
                        .skipLocked()
                        .fetch();

                template.execute(new TransactionCallbackWithoutResult() {
                    @Override
                    protected void doInTransactionWithoutResult(TransactionStatus status) {

                        Result<ProductRecord> products2 = ctx.selectFrom(PRODUCT)
                                .orderBy(PRODUCT.PRODUCT_ID)
                                .limit(3)
                                .forUpdate()
                                .skipLocked()
                                .fetch();

                        System.out.println("Second transaction: " + products2);
                    }
                });
                System.out.println("First transaction: " + products1);
            }
        });

        System.out.println("Done!");
    }
}

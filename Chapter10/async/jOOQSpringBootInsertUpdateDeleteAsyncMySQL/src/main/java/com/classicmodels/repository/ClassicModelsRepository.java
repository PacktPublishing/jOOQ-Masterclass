package com.classicmodels.repository;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Top3product.TOP3PRODUCT;
import org.jooq.DSLContext;
import org.jooq.Record2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

@Repository
public class ClassicModelsRepository {

    private static final Logger logger = Logger.getLogger(ClassicModelsRepository.class.getName());

    private final DSLContext ctx;
    private final TransactionTemplate txTemplate;

    public ClassicModelsRepository(TransactionTemplate txTemplate, DSLContext ctx) {
        this.txTemplate = txTemplate;
        this.ctx = ctx;
    }

    @Async
    public CompletableFuture<Void> insertAsync() {

        txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        return CompletableFuture.runAsync(() -> {

            txTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {

                    ctx.deleteFrom(TOP3PRODUCT).execute();

                    List<Record2<Long, String>> results = ctx.select(
                            ORDERDETAIL.PRODUCT_ID, PRODUCT.PRODUCT_NAME)
                            .from(ORDERDETAIL)
                            .join(PRODUCT).onKey()
                            .groupBy(ORDERDETAIL.QUANTITY_ORDERED)
                            .orderBy(ORDERDETAIL.QUANTITY_ORDERED.desc())
                            .limit(3)
                            .fetch();

                    var insert = ctx.insertInto(TOP3PRODUCT, TOP3PRODUCT.fields());
                    for (Record2<Long, String> result : results) {
                        insert.values(result.valuesRow().fields()).onDuplicateKeyIgnore().execute();
                    }
                }
            });
        });
    }
}

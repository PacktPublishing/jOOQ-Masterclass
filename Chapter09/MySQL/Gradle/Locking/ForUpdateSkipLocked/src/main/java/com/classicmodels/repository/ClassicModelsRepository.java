package com.classicmodels.repository;

import java.util.logging.Logger;
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

    private static final Logger log = Logger.getLogger(ClassicModelsRepository.class.getName());

    private final DSLContext ctx;
    private final TransactionTemplate template;

    public ClassicModelsRepository(DSLContext ctx, TransactionTemplate template) {
        this.ctx = ctx;
        this.template = template;
    }

    public void fetchProductsViaTwoTransactions() throws InterruptedException {
        Thread tA = new Thread(() -> {
            template.setPropagationBehavior(
                    TransactionDefinition.PROPAGATION_REQUIRES_NEW);

            template.execute(new TransactionCallbackWithoutResult() {

                @Override
                protected void doInTransactionWithoutResult(
                        TransactionStatus status) {

                    log.info("Starting first transaction (A) ...");

                    Result<ProductRecord> products1 = ctx.selectFrom(PRODUCT)
                            .where(PRODUCT.PRODUCT_DESCRIPTION.eq("PENDING"))
                            .orderBy(PRODUCT.PRODUCT_ID)
                            .limit(3)
                            .forUpdate()
                            .skipLocked()
                            .fetch();

                    try {
                        log.info("Holding in place first transaction (A) for 10s ...");
                        Thread.sleep(10000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }

                    log.info("First transaction (A) locked the following products ...");
                    log.info(() -> "Products (A):\n" + products1);
                }
            });

            log.info("First transaction (A) committed!");
        });

        Thread tB = new Thread(() -> {
            template.setPropagationBehavior(
                    TransactionDefinition.PROPAGATION_REQUIRES_NEW);

            template.execute(new TransactionCallbackWithoutResult() {

                @Override
                protected void doInTransactionWithoutResult(
                        TransactionStatus status) {

                    log.info("Starting second transaction (B) ...");

                    Result<ProductRecord> products2 = ctx.selectFrom(PRODUCT)
                            .where(PRODUCT.PRODUCT_DESCRIPTION.eq("PENDING"))
                            .orderBy(PRODUCT.PRODUCT_ID)
                            .limit(3)
                            .forUpdate()
                            .skipLocked()
                            .fetch();

                    try {
                        log.info("Holding in place second transaction (B) for 10s ...");
                        Thread.sleep(10000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }

                    log.info("Second transaction (B) locked the following products ...");
                    log.info(() -> "Products (B):\n" + products2);
                }
            });

            log.info("Second transaction (B) committed!");
        });

        tA.start();
        Thread.sleep(5000);
        tB.start();

        tA.join();
        tB.join();
    }
}

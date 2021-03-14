package com.classicmodels.repository;

import java.util.logging.Logger;
import static jooq.generated.tables.Productline.PRODUCTLINE;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

// This app ends with an exception: 
        // MySQLTransactionRollbackException: Lock wait timeout exceeded; try restarting transaction

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

                    ctx.selectFrom(PRODUCTLINE)
                            .where(PRODUCTLINE.PRODUCT_LINE.eq("Classic Cars"))
                            .forUpdate()
                            .fetchSingle();

                    try {
                        log.info("Holding in place first transaction (A) for 60s ...");
                        Thread.sleep(60000); // this should be beyond MySQL innodb_lock_wait_timeout (50s)
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
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

                    ctx.selectFrom(PRODUCTLINE)
                            .where(PRODUCTLINE.PRODUCT_LINE.eq("Classic Cars"))
                            .forUpdate() // lock cannot be acquired since tA is holding it
                            .fetchSingle();
                }
            });

            log.info("Second transaction (B) committed!");
        });

        tA.start();
        Thread.sleep(2000);
        tB.start();

        tA.join();
        tB.join();
    }
}

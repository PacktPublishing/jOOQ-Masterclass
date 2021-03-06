package com.classicmodels.repository;

import java.util.logging.Logger;
import static jooq.generated.tables.Productline.PRODUCTLINE;
import static jooq.generated.tables.Productlinedetail.PRODUCTLINEDETAIL;
import jooq.generated.tables.records.ProductlineRecord;
import jooq.generated.tables.records.ProductlinedetailRecord;
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

                    ProductlinedetailRecord plr = ctx.selectFrom(PRODUCTLINEDETAIL)
                            .where(PRODUCTLINEDETAIL.PRODUCT_LINE.eq("Classic Cars"))
                            .forShare()
                            .fetchSingle();

                    try {
                        log.info("Holding in place first transaction (A) for 10s ...");
                        Thread.sleep(10000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }

                    log.info("First transaction (A) attempts to update the product ...");
                    plr.setLineCapacity("566B"); // this cannot be done, transaction (B) holds the lock                                        
                    plr.update();
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

                    ProductlinedetailRecord plr = ctx.selectFrom(PRODUCTLINEDETAIL)
                            .where(PRODUCTLINEDETAIL.PRODUCT_LINE.eq("Classic Cars"))
                            //.forShare()
                            .fetchSingle(); // get the lock

                    try {
                        log.info("Holding in place second transaction (B) for 10s ...");
                        Thread.sleep(10000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }

                    log.info("Second transaction (B) attempts to update the product ...");
                    plr.setLineType(2); // this cannot be done, transaction (A) holds the lock                    
                    plr.update();
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

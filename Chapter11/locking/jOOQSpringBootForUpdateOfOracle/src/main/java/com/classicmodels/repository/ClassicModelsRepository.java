package com.classicmodels.repository;

import java.util.logging.Logger;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Productline.PRODUCTLINE;
import org.jooq.DSLContext;
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

                    ctx.select(PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.CODE, 
                            PRODUCT.PRODUCT_LINE, PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_SCALE)
                            .from(PRODUCTLINE)
                            .join(PRODUCT)
                            .onKey()
                            // lock only rows from PRODUCTLINE
                            .forUpdate().of(PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.CODE)                            
                            .fetch();

                    try {
                        log.info("Holding in place first transaction (A) for 10s ...");
                        Thread.sleep(10000);
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

                    ctx.select(PRODUCTLINE.PRODUCT_LINE, PRODUCTLINE.CODE, 
                            PRODUCT.PRODUCT_LINE, PRODUCT.PRODUCT_NAME, PRODUCT.PRODUCT_SCALE)
                            .from(PRODUCTLINE)
                            .join(PRODUCT)
                            .onKey()
                            // lock only rows from PRODUCT
                            .forUpdate().of(PRODUCT.PRODUCT_NAME) // if of() is removed then the lock 
                            .fetch();                             // cannot be acquired since tA holds the lock                            
                                                                  // on PRODUCTLINE rows and forUpdate() attempts                    
                }                                                 // to lock both tables
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

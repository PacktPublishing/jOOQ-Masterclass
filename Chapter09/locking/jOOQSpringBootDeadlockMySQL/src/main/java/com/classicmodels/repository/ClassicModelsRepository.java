package com.classicmodels.repository;

import java.util.logging.Logger;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Sale.SALE;
import jooq.generated.tables.records.OrderRecord;
import jooq.generated.tables.records.SaleRecord;
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

                    SaleRecord sr = ctx.selectFrom(SALE)
                            .where(SALE.SALE_ID.eq(1L))
                            .fetchSingle();
                    sr.setSale(5644.33);
                    sr.update();

                    try {
                        log.info("Holding in place first transaction (A) for 10s ...");
                        Thread.sleep(10000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }

                    log.info("First transaction (A) attempts to update the product ...");
                    OrderRecord or = ctx.selectFrom(ORDER)
                            .where(ORDER.ORDER_ID.eq(10100L))
                            .fetchSingle();
                    or.setCustomerNumber(131L); // this cannot be done, transaction (B) holds the lock                                        
                    or.update();
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

                    OrderRecord or = ctx.selectFrom(ORDER)
                            .where(ORDER.ORDER_ID.eq(10100L))
                            .fetchSingle();
                    or.setCustomerNumber(145L);
                    or.update();

                    try {
                        log.info("Holding in place second transaction (B) for 10s ...");
                        Thread.sleep(10000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }

                    log.info("Second transaction (B) attempts to update the product ...");
                    SaleRecord sr = ctx.selectFrom(SALE) // this cannot be done, transaction (A) holds the lock
                            .where(SALE.SALE_ID.eq(1L))
                            .fetchSingle();
                    sr.setSale(1234.44);
                    sr.update();
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

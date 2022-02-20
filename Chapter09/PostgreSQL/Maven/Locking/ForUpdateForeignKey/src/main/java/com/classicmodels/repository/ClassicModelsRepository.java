package com.classicmodels.repository;

import java.util.logging.Logger;
import static jooq.generated.tables.Employee.EMPLOYEE;
import static jooq.generated.tables.EmployeeStatus.EMPLOYEE_STATUS;
import static jooq.generated.tables.Sale.SALE;
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
                            .forUpdate()
                            .fetchSingle();

                    try {
                        log.info("Holding in place first transaction (A) for 60s ...");
                        Thread.sleep(60000);
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
                    log.info("Second transaction (B) successfully updates "
                            + "the product name and deletes from employee_status ...");

                    ctx.update(EMPLOYEE)
                            .set(EMPLOYEE.EMAIL, "ghernandez@yahoo.com")
                            .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(1370L))
                            .execute();
                    
                    ctx.deleteFrom(EMPLOYEE_STATUS)
                            .where(EMPLOYEE_STATUS.EMPLOYEE_NUMBER.eq(1370L))
                            .execute();

                    log.info("Second transaction (B) attempts to update the product PK but it must wait for transaction (A) to release the lock  ...");
                    log.info("Waiting ... ... ... ... ... ... ... ... ... ");

                    ctx.update(EMPLOYEE)
                            .set(EMPLOYEE.EMPLOYEE_NUMBER, 9999L)
                            .where(EMPLOYEE.EMPLOYEE_NUMBER.eq(1370L))
                            .execute();
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

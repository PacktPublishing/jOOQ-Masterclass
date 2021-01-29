package com.classicmodels.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Sale.SALE;
import org.jooq.DSLContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ClassicModelsRepository {

    private static final Logger logger = Logger.getLogger(ClassicModelsRepository.class.getName());

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    @Async
    public CompletableFuture<Void> insertAsync() {

        return CompletableFuture.runAsync(() -> {

            for (int i = 0; i < 5; i++) {
                ctx.insertInto(ORDER)
                        .values(null, // primary key is auto-generated
                                LocalDate.of(2003, 2, 12), LocalDate.of(2003, 3, 1),
                                LocalDate.of(2003, 2, 27), "Shipped",
                                "New order inserted ...", 363L)
                        .execute();

                logger.log(Level.INFO, "Insert number:{0} by {1}",
                        new Object[]{i, Thread.currentThread().getName()});

                try {
                    Thread.sleep(3000 * Math.round(10)); // sleep between inserts
                } catch (InterruptedException ex) {
                }
            }
        });
    }

    @Async
    public CompletableFuture<Void> updateAsync() {

        return CompletableFuture.runAsync(() -> {

            for (int i = 0; i < 5; i++) {
                ctx.update(PRODUCT)
                        .set(PRODUCT.BUY_PRICE, BigDecimal.valueOf(Math.rint(1000)))
                        .where(PRODUCT.PRODUCT_ID.eq(1L))
                        .execute();

                logger.log(Level.INFO, "Update number:{0} by {1}",
                        new Object[]{i, Thread.currentThread().getName()});

                try {
                    Thread.sleep(2000 * Math.round(10)); // sleep between updates
                } catch (InterruptedException ex) {
                }
            }
        });
    }

    @Async
    public CompletableFuture<Void> deleteAsync() {

        return CompletableFuture.runAsync(() -> {

            long id = 1L;

            for (int i = 0; i < 5; i++) {
                ctx.deleteFrom(SALE)
                        .where(SALE.SALE_ID.eq(id))
                        .execute();

                id++;

                logger.log(Level.INFO, "Delete number:{0} by {1}",
                        new Object[]{i, Thread.currentThread().getName()});

                try {
                    Thread.sleep(1000 * Math.round(10)); // sleep between deletes
                } catch (InterruptedException ex) {
                }
            }
        });
    }
}

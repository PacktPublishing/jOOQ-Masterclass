package com.classicmodels.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Orderdetail.ORDERDETAIL;
import static jooq.generated.tables.Product.PRODUCT;
import static jooq.generated.tables.Top3product.TOP3PRODUCT;
import org.jooq.DSLContext;
import org.jooq.Record2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {
    
    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    @Async
    public CompletableFuture<Void> updateTop3ProductsAsync() {

        return CompletableFuture.runAsync(() -> {

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
        });
    }

    @Async
    public CompletableFuture<Void> insertUpdateDeleteOrder() {

        return CompletableFuture.supplyAsync(() -> {
            return ctx.insertInto(ORDER)
                    .values(null,
                            LocalDate.of(2003, 2, 12), LocalDate.of(2003, 3, 1),
                            LocalDate.of(2003, 2, 27), "Shipped",
                            "New order inserted ...", 363L, BigDecimal.ZERO)
                    .returning()
                    .fetchOne();
        }).thenApply(order -> {
            order.setStatus("ON HOLD");
            order.setComments("Reverted to on hold ...");
            ctx.executeUpdate(order);
            
            return order.getOrderId();            
        }).thenAccept(id -> ctx.deleteFrom(ORDER)
                .where(ORDER.ORDER_ID.eq(id)).execute());
    }
}

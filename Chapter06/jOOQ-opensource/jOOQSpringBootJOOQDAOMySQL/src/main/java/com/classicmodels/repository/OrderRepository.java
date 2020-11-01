package com.classicmodels.repository;

import java.util.List;
import static jooq.generated.tables.Order.ORDER;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class OrderRepository extends
        jooq.generated.tables.daos.OrderRepository {

    private final DSLContext ctx;

    public OrderRepository(DSLContext ctx) {
        super(ctx.configuration());
        this.ctx = ctx;
    }

    public List<String> findOrderStatus() {

        List<String> result = ctx.select(ORDER.STATUS)
                .from(ORDER)
                .fetchInto(String.class);

        return result;
    }
}

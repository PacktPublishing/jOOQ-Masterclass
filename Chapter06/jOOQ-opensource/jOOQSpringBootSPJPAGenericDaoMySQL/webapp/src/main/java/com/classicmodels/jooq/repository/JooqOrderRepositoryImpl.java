package com.classicmodels.jooq.repository;

import java.util.List;
import static jooq.generated.tables.Order.ORDER;
import jooq.generated.tables.pojos.Order;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
public class JooqOrderRepositoryImpl implements JooqOrderRepository {

    private final DSLContext ctx;

    public JooqOrderRepositoryImpl(DSLContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public List<String> findOrderStatus() {

        List<String> result = ctx.select(ORDER.STATUS)
                .from(ORDER)
                .fetchInto(String.class);

        return result;
    }

    @Override
    public Order findOrderById(Long id) {

        Order result = ctx.selectFrom(ORDER)
                .where(ORDER.ORDER_ID.eq(id))
                .fetchOneInto(Order.class);

        return result;

    }
}
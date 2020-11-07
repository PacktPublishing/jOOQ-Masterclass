package com.classicmodels.repository;

import java.util.List;
import static jooq.generated.tables.Order.ORDER;
import jooq.generated.tables.daos.OrderRepository;
import jooq.generated.tables.pojos.Order;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class OrderRepositoryImpl extends OrderRepository {

    private final DSLContext ctx;

    public OrderRepositoryImpl(DSLContext ctx) {
        super(ctx.configuration());
        this.ctx = ctx;
    }

    public List<Order> findOrderDescByDate() {

        List<Order> result = ctx.selectFrom(ORDER)
                .orderBy(ORDER.ORDER_DATE.desc())
                .fetchInto(Order.class);

        return result;
    }
}

package com.classicmodels.repository;

import java.time.LocalDate;
import java.util.List;
import static jooq.generated.tables.Order.ORDER;
import jooq.generated.tables.pojos.Order;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final DSLContext ctx;

    public OrderRepositoryImpl(DSLContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public List<Order> findOrderDescByDate() {

        List<Order> result = ctx.selectFrom(ORDER)
                .orderBy(ORDER.ORDER_DATE.desc())
                .fetchInto(Order.class);

        return result;
    }

    @Override
    public List<Order> findOrderBetweenDate(LocalDate sd, LocalDate ed) {

        List<Order> result = ctx.selectFrom(ORDER)
                .where(ORDER.ORDER_DATE.between(sd, ed))
                .fetchInto(Order.class);

        return result;

    }

    @Override
    public List<Order> findAll() {

        return ctx.selectFrom(ORDER)
                .fetchInto(Order.class);
    }

    @Override
    public void deleteById(Long id) {

        ctx.deleteFrom(ORDER)
                .where(ORDER.ORDER_ID.eq(id))
                .execute();
    }
}
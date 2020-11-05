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
    public List<String> findOrderStatuses() {

        List<String> result = ctx.select(ORDER.STATUS)
                .from(ORDER)
                .fetchInto(String.class);

        return result;
    }

    @Override
    public List<Order> findOrderByShippedDate(LocalDate date) {

        List<Order> result = ctx.selectFrom(ORDER)
                .where(ORDER.SHIPPED_DATE.eq(date))
                .fetchInto(Order.class);

        return result;

    }
}

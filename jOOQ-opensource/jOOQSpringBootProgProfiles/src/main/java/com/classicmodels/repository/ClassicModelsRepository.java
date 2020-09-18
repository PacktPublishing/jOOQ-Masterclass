package com.classicmodels.repository;

import java.util.List;
import java.util.Map;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Order.ORDER;
import jooq.generated.tables.pojos.Order;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {

    private final DSLContext create;

    public ClassicModelsRepository(DSLContext create) {
        this.create = create;
    }
    
    public Map<Long, List<Order>> findAndGroupOrdersByCustomerId() {

        Map<Long, List<Order>> result = create.selectFrom(ORDER)
                .orderBy(ORDER.COMMENTS.asc().nullsLast())
                .fetchGroups(CUSTOMER.CUSTOMER_NUMBER, Order.class);

        return result;
    }
}

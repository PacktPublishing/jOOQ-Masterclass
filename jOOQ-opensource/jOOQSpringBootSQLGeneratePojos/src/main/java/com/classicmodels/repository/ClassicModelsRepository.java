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
   
    public Order findOrder(Long orderId) {

        Order result = create.selectFrom(ORDER)
                .where(ORDER.ORDER_ID.eq(orderId))
                .fetchOneInto(Order.class);

        return result;
    }

    public Map<Long, List<Order>> findAndGroupOrdersByCustomerId() {

        Map<Long, List<Order>> result = create.selectFrom(ORDER)
                .fetchGroups(CUSTOMER.CUSTOMER_NUMBER, Order.class);

        return result;
    }

    public List<Order> findCustomerOrders(Long customerNumber) {

        List<Order> result = create.selectFrom(ORDER)
                .where(ORDER.CUSTOMER_NUMBER.eq(customerNumber))
                .fetchInto(Order.class);

        return result;
    }

}

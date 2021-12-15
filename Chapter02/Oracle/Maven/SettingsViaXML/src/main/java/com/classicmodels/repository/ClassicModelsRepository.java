package com.classicmodels.repository;

import com.classicmodels.pojo.CustomerAndOrder;
import java.util.List;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Order.ORDER;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public List<CustomerAndOrder> findCustomersAndOrders() {

        /* Using jOOQ to build the typesafe SQL and to execute it */
        List<CustomerAndOrder> result = ctx.select(CUSTOMER.CUSTOMER_NAME, ORDER.ORDER_DATE)
                .from(ORDER)
                .innerJoin(CUSTOMER).using(CUSTOMER.CUSTOMER_NUMBER)
                .orderBy(ORDER.ORDER_DATE.desc())
                .fetchInto(CustomerAndOrder.class); // or, fetch().into(CustomerAndOrder.class)

        return result;
    }
}
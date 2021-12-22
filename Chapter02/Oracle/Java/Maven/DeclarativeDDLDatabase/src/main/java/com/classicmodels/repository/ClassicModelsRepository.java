package com.classicmodels.repository;

import com.classicmodels.pojo.Order;
import com.classicmodels.pojo.CustomerAndOrder;
import com.classicmodels.pojo.Office;
import java.time.LocalDate;
import java.util.List;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Office.OFFICE;
import static jooq.generated.tables.Order.ORDER;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {

    private final DSLContext ctx;

    public ClassicModelsRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public List<Office> findOfficesInTerritory(String territory) {

        /* Using jOOQ to build the typesafe SQL and to execute it */
        List<Office> result = ctx.selectFrom(OFFICE) // or, ctx.select().from(OFFICE)
                .where(OFFICE.TERRITORY.eq(territory))
                .fetchInto(Office.class); // or, fetch().into(Office.class)

        return result;
    }

    public List<Order> findOrdersByRequiredDate(LocalDate startDate, LocalDate endDate) {

        /* Using jOOQ to build the typesafe SQL and to execute it */
        List<Order> result = ctx.selectFrom(ORDER)
                .where(ORDER.REQUIRED_DATE.between(startDate, endDate))                
                .fetchInto(Order.class); // or, fetch().into(Order.class)

        return result;
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
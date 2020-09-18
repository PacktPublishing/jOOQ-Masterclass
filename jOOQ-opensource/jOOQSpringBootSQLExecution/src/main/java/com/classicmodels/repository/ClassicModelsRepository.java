package com.classicmodels.repository;

import com.classicmodels.pojo.DelayedPayment;
import com.classicmodels.pojo.Order;
import com.classicmodels.pojo.OrderAndNextOrderDate;
import java.time.LocalDate;
import java.util.List;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Order.ORDER;
import static jooq.generated.tables.Payment.PAYMENT;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.lead;
import static org.jooq.impl.DSL.partitionBy;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {

    private final DSLContext create;

    public ClassicModelsRepository(DSLContext create) {
        this.create = create;
    }
  
    public Order findOrder(Long orderId) {

        /* Using jOOQ to build and execute the typesafe SQL */
        Order result = create.selectFrom(ORDER)
                .where(ORDER.ORDER_ID.eq(orderId))
                .fetchOneInto(Order.class);

        return result;
    }

    public List<DelayedPayment> findDelayedPayments(LocalDate startDate, LocalDate endDate) {

        /* Using jOOQ to build and execute the typesafe SQL */
        List<DelayedPayment> result = create.select(CUSTOMER.CUSTOMER_NAME, PAYMENT.PAYMENT_DATE,
                PAYMENT.CACHING_DATE, PAYMENT.INVOICE_AMOUNT)
                .from(PAYMENT)
                .join(CUSTOMER).on(PAYMENT.CUSTOMER_NUMBER.eq(CUSTOMER.CUSTOMER_NUMBER))
                .where(PAYMENT.PAYMENT_DATE.between(startDate).and(endDate))
                .and(PAYMENT.PAYMENT_DATE.isDistinctFrom(PAYMENT.CACHING_DATE))
                .fetchInto(DelayedPayment.class);  // or fetch().into(DelayedPayment.class)  

        return result;
    }

    public List<OrderAndNextOrderDate> findOrderAndNextOrderDate() {

        /* Using jOOQ to build and execute the typesafe SQL */        
        List<OrderAndNextOrderDate> result = create.select(CUSTOMER.CUSTOMER_NAME, ORDER.ORDER_DATE,
                lead(ORDER.ORDER_DATE, 1).over(partitionBy(ORDER.CUSTOMER_NUMBER)
                        .orderBy(ORDER.ORDER_DATE)).as("NEXT_ORDER_DATE"))
                .from(ORDER)
                .join(CUSTOMER).using(ORDER.CUSTOMER_NUMBER)
                .fetch().into(OrderAndNextOrderDate.class); // or fetchInto(OrderAndNextOrderDate.class)

        return result;
    }
}

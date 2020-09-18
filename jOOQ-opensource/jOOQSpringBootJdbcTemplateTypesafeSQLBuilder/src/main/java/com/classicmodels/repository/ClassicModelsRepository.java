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
import org.jooq.Query;
import static org.jooq.impl.DSL.lead;
import static org.jooq.impl.DSL.partitionBy;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {

    private final DSLContext create;
    private final JdbcTemplate jdbcTemplate;

    public ClassicModelsRepository(DSLContext create, JdbcTemplate jdbcTemplate) {
        this.create = create;
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public Order findOrder(Long orderId) {

        /* Using only JdbcTemplate */    
        /*
        String sql = """
                     SELECT * FROM `order` WHERE order_id=?
                     """;

        Order result = (Order) jdbcTemplate.queryForObject(sql, new Object[]{orderId},
                new BeanPropertyRowMapper(Order.class));                 
        */
        
        /* Using jOOQ to build the typesafe SQL and JdbcTemplate to execute it */                
        Query query = create.selectFrom(ORDER)
                .where(ORDER.ORDER_ID.eq(orderId));

       Order result = (Order) jdbcTemplate.queryForObject(query.getSQL(),
                query.getBindValues().toArray(), new BeanPropertyRowMapper(Order.class));
       
        return result;
    }
        
    public List<DelayedPayment> findDelayedPayments(LocalDate startDate, LocalDate endDate) {

        /* Using only JdbcTemplate */
        
        // Explicit join
        /*
        String sql = """  
                   SELECT c.customer_name,
                              p.payment_date,
                              p.caching_date,
                              p.invoice_amount
                       FROM payment p
                       JOIN customer c ON p.customer_number = c.customer_number
                       WHERE (p.payment_date BETWEEN ? AND ?
                              AND (NOT(p.payment_date <=> p.caching_date)))
                   """;                                
        
        // Same query using implicit join         
        String sql = """
                     SELECT c.customer_name,
                                p.payment_date,
                                p.caching_date,
                                p.invoice_amount
                         FROM payment p,
                              customer c
                         WHERE p.customer_number = c.customer_number
                           AND (p.payment_date BETWEEN ? AND ?
                                AND (NOT(p.payment_date <=> p.caching_date)))
                     """;       
        
        List<DelayedPayment> result = jdbcTemplate.query(sql, new Object[]{startDate, endDate},
                new BeanPropertyRowMapper(DelayedPayment.class));
        */
        
        /* Using jOOQ to build the typesafe SQL and JdbcTemplate to execute it */        
        // Explicit join
        /*
        Query query = create.select(CUSTOMER.CUSTOMER_NAME, PAYMENT.PAYMENT_DATE,
                                    PAYMENT.CACHING_DATE, PAYMENT.INVOICE_AMOUNT)
                .from(PAYMENT)
                .join(CUSTOMER).on(PAYMENT.CUSTOMER_NUMBER.eq(CUSTOMER.CUSTOMER_NUMBER))
                .where(PAYMENT.PAYMENT_DATE.between(startDate).and(endDate))
                .and(PAYMENT.PAYMENT_DATE.isDistinctFrom(PAYMENT.CACHING_DATE));    
        */
        
        // Same query using implicit join             
        Query query = create.select(PAYMENT.customer().CUSTOMER_NAME, PAYMENT.PAYMENT_DATE,
                                    PAYMENT.CACHING_DATE, PAYMENT.INVOICE_AMOUNT)
                .from(PAYMENT)                
                .where(PAYMENT.PAYMENT_DATE.between(startDate).and(endDate))
                .and(PAYMENT.PAYMENT_DATE.isDistinctFrom(PAYMENT.CACHING_DATE));       
        
        List<DelayedPayment> result = jdbcTemplate.query(query.getSQL(),
                query.getBindValues().toArray(), new BeanPropertyRowMapper(DelayedPayment.class));
        
        return result;
    }
    
    public List<OrderAndNextOrderDate> findOrderAndNextOrderDate() {
        
        /* Using only JdbcTemplate */
        /*
        String sql = """
                   SELECT customer_name,
                          order_date,
                          LEAD(order_date, 1) OVER (PARTITION BY customer_number
                                                    ORDER BY order_date) next_order_date
                   FROM `order`
                   INNER JOIN customer USING (customer_number);
                   """;

        List<OrderAndNextOrderDate> result = jdbcTemplate.query(sql,
                new BeanPropertyRowMapper(OrderAndNextOrderDate.class));
         */
        
        /* Using jOOQ to build the typesafe SQL and JdbcTemplate to execute it */
        Query query = create.select(CUSTOMER.CUSTOMER_NAME, ORDER.ORDER_DATE,
                lead(ORDER.ORDER_DATE, 1).over(partitionBy(ORDER.CUSTOMER_NUMBER)
                        .orderBy(ORDER.ORDER_DATE)).as("NEXT_ORDER_DATE"))
                .from(ORDER)
                .join(CUSTOMER).using(ORDER.CUSTOMER_NUMBER);
       
        List<OrderAndNextOrderDate> result = jdbcTemplate.query(query.getSQL(),
                new BeanPropertyRowMapper(OrderAndNextOrderDate.class));

        return result;
    }
}

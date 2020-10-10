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
import org.jooq.Field;
import org.jooq.Query;
import static org.jooq.impl.DSL.asterisk;
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
                     SELECT * FROM "ORDER" WHERE ORDER_ID=?
                     """;                

        Order result = (Order) jdbcTemplate.queryForObject(sql, new Object[]{orderId},
                new BeanPropertyRowMapper(Order.class));                 
        */
        
        /* Using jOOQ to build the SQL and JdbcTemplate to execute it */             
        Query query = create.select(asterisk())
                .from(ORDER)
                .where(ORDER.ORDER_ID.eq(orderId));

        Order result = (Order) jdbcTemplate.queryForObject(query.getSQL(),
                query.getBindValues().toArray(), new BeanPropertyRowMapper(Order.class));
       
        return result;
    }           
   
    public List<OrderAndNextOrderDate> findOrderAndNextOrderDate() {
        
        /* Using only JdbcTemplate */
        /*
        String sql = """
                   SELECT C.CUSTOMER_NAME,
                          O.ORDER_DATE,
                          LEAD(O.ORDER_DATE, 1) OVER (PARTITION BY O.CUSTOMER_NUMBER
                                                    ORDER BY O.ORDER_DATE) NEXT_ORDER_DATE
                   FROM `ORDER` O
                   INNER JOIN CUSTOMER C
                     ON C.CUSTOMER_NUMBER = O.CUSTOMER_NUMBER
                   """;

        List<OrderAndNextOrderDate> result = jdbcTemplate.query(sql,
                new BeanPropertyRowMapper(OrderAndNextOrderDate.class));
        */
        
        /* Using jOOQ to build the SQL and JdbcTemplate to execute it */        
        Field<LocalDate> orderDate = ORDER.ORDER_DATE;
        
        Query query = create.select(CUSTOMER.CUSTOMER_NAME, orderDate,
                lead(orderDate, 1).over(partitionBy(ORDER.CUSTOMER_NUMBER)
                        .orderBy(orderDate)).as("NEXT_ORDER_DATE"))
                .from(ORDER)
                .join(CUSTOMER)
                   .on(CUSTOMER.CUSTOMER_NUMBER.eq(ORDER.CUSTOMER_NUMBER));

        List<OrderAndNextOrderDate> result = jdbcTemplate.query(query.getSQL(),
                new BeanPropertyRowMapper(OrderAndNextOrderDate.class));

        return result;
    }
    
    public List<DelayedPayment> findDelayedPayments(LocalDate startDate, LocalDate endDate) {
        
        /* Using only JdbcTemplate */ 
        /*
        String sql = """  
                   SELECT C.CUSTOMER_NAME,
                          P.PAYMENT_DATE,
                          P.CACHING_DATE,
                          P.INVOICE_AMOUNT
                       FROM PAYMENT P
                       JOIN CUSTOMER C ON P.CUSTOMER_NUMBER = C.CUSTOMER_NUMBER
                       WHERE (P.PAYMENT_DATE BETWEEN cast(? AS date) AND cast(? AS date)
                              AND NOT EXISTS (SELECT P.PAYMENT_DATE "x" 
                                              FROM DUAL INTERSECT SELECT P.CACHING_DATE "x" FROM DUAL)) 
                   """;                                
        
        List<DelayedPayment> result = jdbcTemplate.query(sql, new Object[]{startDate, endDate},
                new BeanPropertyRowMapper(DelayedPayment.class));
        */
        
        /* Using jOOQ to build the typesafe SQL and JdbcTemplate to execute it */                        
        Query query = create.select(CUSTOMER.CUSTOMER_NAME, PAYMENT.PAYMENT_DATE,
                                    PAYMENT.CACHING_DATE, PAYMENT.INVOICE_AMOUNT)
                .from(PAYMENT)
                .join(CUSTOMER).on(PAYMENT.CUSTOMER_NUMBER.eq(CUSTOMER.CUSTOMER_NUMBER))
                .where(PAYMENT.PAYMENT_DATE.between(startDate).and(endDate))
                .and(PAYMENT.PAYMENT_DATE.isDistinctFrom(PAYMENT.CACHING_DATE));    
        
        
        List<DelayedPayment> result = jdbcTemplate.query(query.getSQL(),
                query.getBindValues().toArray(), new BeanPropertyRowMapper(DelayedPayment.class));
        
        return result;
    }
}
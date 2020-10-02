package com.classicmodels.repository;

import com.classicmodels.pojo.DelayedPayment;
import com.classicmodels.pojo.Order;
import com.classicmodels.pojo.OrderAndNextOrderDate;
import java.time.LocalDate;
import java.util.List;
import org.jooq.DSLContext;
import org.jooq.Name;
import org.jooq.Query;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.lead;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.partitionBy;
import static org.jooq.impl.DSL.table;
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
                     SELECT * FROM `ORDER` WHERE ORDER_ID=?
                     """;        
        
        Order result = (Order) jdbcTemplate.queryForObject(sql, new Object[]{orderId},
                new BeanPropertyRowMapper(Order.class));                 
        */
                
        /* Using jOOQ to build the SQL and JdbcTemplate to execute it */  
        Query query = create.selectFrom(table(name("ORDER")))
                .where(field("ORDER_ID").eq(orderId));

        Order result = (Order) jdbcTemplate.queryForObject(query.getSQL(),
                query.getBindValues().toArray(), new BeanPropertyRowMapper(Order.class));
       
        return result;
    }
    
        public List<OrderAndNextOrderDate> findOrderAndNextOrderDate() {
        
        /* Using only JdbcTemplate */   
        /*
        String sql = """                   
                   SELECT CUSTOMER_NAME,
                          ORDER_DATE,
                          LEAD(ORDER_DATE, 1) OVER (PARTITION BY CUSTOMER_NUMBER
                                                    ORDER BY ORDER_DATE) NEXT_ORDER_DATE
                   FROM `ORDER`
                   INNER JOIN CUSTOMER USING (CUSTOMER_NUMBER)
                   """;

        List<OrderAndNextOrderDate> result = jdbcTemplate.query(sql,
                new BeanPropertyRowMapper(OrderAndNextOrderDate.class));         
        */
        
        /* Using jOOQ to build the SQL and JdbcTemplate to execute it */        
        Name no = name("ORDER", "ORDER_DATE");        
         Query query = create.select(field("CUSTOMER.CUSTOMER_NAME"), field(no),
                lead(field(no), 1).over(partitionBy(field("CUSTOMER_NUMBER"))
                        .orderBy(field(no))).as("NEXT_ORDER_DATE"))
                .from(table(name("ORDER")))
                .join(table("CUSTOMER")).using(field("CUSTOMER_NUMBER"));

        /* using unqualified names (use this approach with caution to avoid columns names ambiguities) */
        /*        
        Query query = create.select(field("CUSTOMER_NAME"), field("ORDER_DATE"),
                lead(field("ORDER_DATE"), 1).over(partitionBy(field("CUSTOMER_NUMBER"))
                        .orderBy(field("ORDER_DATE"))).as("NEXT_ORDER_DATE"))                                        
                .from(table(name("ORDER")))
                .join(table("CUSTOMER")).using(field("CUSTOMER_NUMBER"));
        */ 
        
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
                       JOIN CUSTOMER C ON p.CUSTOMER_NUMBER = C.CUSTOMER_NUMBER
                       WHERE (P.PAYMENT_DATE BETWEEN ? AND ?
                              AND (NOT(P.PAYMENT_DATE <=> P.CACHING_DATE))) 
                   """;        
                
        List<DelayedPayment> result = jdbcTemplate.query(sql, new Object[]{startDate, endDate},
                new BeanPropertyRowMapper(DelayedPayment.class));
        */
        
        /* Using jOOQ to build the SQL and JdbcTemplate to execute it */        
        Query query = create.select(field("CUSTOMER.CUSTOMER_NAME"), field("PAYMENT.PAYMENT_DATE"),
                                    field("PAYMENT.CACHING_DATE"), field("PAYMENT.INVOICE_AMOUNT"))
                .from(table("PAYMENT"))
                .join(table("CUSTOMER"))
                .on(field("PAYMENT.CUSTOMER_NUMBER").eq(field("CUSTOMER.CUSTOMER_NUMBER")))
                .where(field("PAYMENT.PAYMENT_DATE").between(startDate).and(endDate))
                .and(field("PAYMENT.PAYMENT_DATE").isDistinctFrom(field("PAYMENT.CACHING_DATE")));

        List<DelayedPayment> result = jdbcTemplate.query(query.getSQL(),
                query.getBindValues().toArray(), new BeanPropertyRowMapper(DelayedPayment.class));

        return result;
    }  
}
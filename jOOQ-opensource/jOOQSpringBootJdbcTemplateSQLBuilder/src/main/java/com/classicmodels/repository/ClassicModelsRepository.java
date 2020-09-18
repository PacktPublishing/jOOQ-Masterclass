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
                     SELECT * FROM `order` WHERE order_id=?
                     """;

        Order result = (Order) jdbcTemplate.queryForObject(sql, new Object[]{orderId},
                new BeanPropertyRowMapper(Order.class));                 
        */
        
        /* Using jOOQ to build the SQL and JdbcTemplate to execute it */                
        Query query = create.selectFrom(table(name("ORDER")))
                .where(field("ORDER.ORDER_ID").eq(orderId));

        Order result = (Order) jdbcTemplate.queryForObject(query.getSQL(),
                query.getBindValues().toArray(), new BeanPropertyRowMapper(Order.class));
       
        return result;
    }
       
    public List<DelayedPayment> findDelayedPayments(LocalDate startDate, LocalDate endDate) {

        /* Using only JdbcTemplate */
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
                
        List<DelayedPayment> result = jdbcTemplate.query(sql, new Object[]{startDate, endDate},
                new BeanPropertyRowMapper(DelayedPayment.class));
        */
        
        /* Using jOOQ to build the SQL and JdbcTemplate to execute it */
        Query query = create.select(field("CUSTOMER.CUSTOMER_NAME"), field("PAYMENT.PAYMENT_DATE"),
                                    field("PAYMENT.CACHING_DATE"), field("PAYMENT.INVOICE_AMOUNT"))
                .from(table("PAYMENT"))
                .join(table("CUSTOMER")).on(field("PAYMENT.CUSTOMER_NUMBER").eq(field("CUSTOMER.CUSTOMER_NUMBER")))
                .where(field("PAYMENT.PAYMENT_DATE").between(startDate).and(endDate))
                .and(field("PAYMENT.PAYMENT_DATE").isDistinctFrom(field("PAYMENT.CACHING_DATE")));

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
        
        /* Using jOOQ to build the SQL and JdbcTemplate to execute it */
        Name no = name("ORDER", "ORDER_DATE");
        Name nc = name("ORDER", "CUSTOMER_NUMBER");
        Query query = create.select(field("CUSTOMER.CUSTOMER_NAME"), field(no),
                lead(field(no), 1).over(partitionBy(field(nc))
                        .orderBy(field(no))).as("NEXT_ORDER_DATE"))
                .from(table(name("ORDER")))
                .join(table("CUSTOMER")).using(field(nc));

        /* using unqualified name */
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
}

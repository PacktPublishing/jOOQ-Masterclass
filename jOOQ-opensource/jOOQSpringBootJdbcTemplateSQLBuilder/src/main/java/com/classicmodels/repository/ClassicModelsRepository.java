package com.classicmodels.repository;

import com.classicmodels.pojo.DelayedPayment;
import com.classicmodels.pojo.Manager;
import com.classicmodels.pojo.CustomerCachingDate;
import java.time.LocalDate;
import java.util.List;
import org.jooq.DSLContext;
import org.jooq.Field;
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

    private final DSLContext ctx;
    private final JdbcTemplate jdbcTemplate;

    public ClassicModelsRepository(DSLContext ctx, JdbcTemplate jdbcTemplate) {
        this.ctx = ctx;
        this.jdbcTemplate = jdbcTemplate;
    }

    public Manager findManager(Long managerId) {

        /* Using only JdbcTemplate */
        /*
        String sql = """
                     SELECT * FROM MANAGER WHERE MANAGER_ID=?
                     """;        
        
        Manager result = (Manager) jdbcTemplate.queryForObject(sql, new Object[]{managerId},
                new BeanPropertyRowMapper(Manager.class));                 
         */
        
        /* Using jOOQ to build the SQL and JdbcTemplate to execute it */
        Query query = ctx.selectFrom(table("MANAGER"))
                .where(field("MANAGER_ID").eq(managerId));

        Manager result = (Manager) jdbcTemplate.queryForObject(query.getSQL(),
                query.getBindValues().toArray(), new BeanPropertyRowMapper(Manager.class));

        return result;
    }

    public List<CustomerCachingDate> findCustomerCachingDate() {

        /* Using only JdbcTemplate */        
        /*
        String sql = """                   
                   SELECT CUSTOMER_NAME,
                          CACHING_DATE,
                          LEAD(CACHING_DATE, 1) OVER (PARTITION BY PAYMENT.CUSTOMER_NUMBER
                                ORDER BY CACHING_DATE) NEXT_CACHING_DATE
                    FROM PAYMENT
                    INNER JOIN CUSTOMER ON CUSTOMER.CUSTOMER_NUMBER = PAYMENT.CUSTOMER_NUMBER
                   """;
        
        List<CustomerCachingDate> result = jdbcTemplate.query(sql,
                new BeanPropertyRowMapper(CustomerCachingDate.class));                  
        */
        
        /* Using jOOQ to build the SQL and JdbcTemplate to execute it */
        Field<?> paymentCachingDate = field("PAYMENT.CACHING_DATE");
        Query query = ctx.select(field("CUSTOMER.CUSTOMER_NAME"), paymentCachingDate,
                lead(paymentCachingDate, 1).over(partitionBy(field("PAYMENT.CUSTOMER_NUMBER"))
                        .orderBy(paymentCachingDate)).as("NEXT_CACHING_DATE"))
                .from(table(name("PAYMENT")))
                .join(table("CUSTOMER"))
                  .on(field("CUSTOMER.CUSTOMER_NUMBER").eq(field("PAYMENT.CUSTOMER_NUMBER")));
        
        List<CustomerCachingDate> result = jdbcTemplate.query(query.getSQL(),
                new BeanPropertyRowMapper(CustomerCachingDate.class));
        
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
                       WHERE (P.PAYMENT_DATE BETWEEN ? AND ?
                              AND (NOT(P.PAYMENT_DATE <=> P.CACHING_DATE))) 
                   """;        
                
        List<DelayedPayment> result = jdbcTemplate.query(sql, new Object[]{startDate, endDate},
                new BeanPropertyRowMapper(DelayedPayment.class));
         */
        
        /* Using jOOQ to build the SQL and JdbcTemplate to execute it */
        Query query = ctx.select(field("CUSTOMER.CUSTOMER_NAME"), field("PAYMENT.PAYMENT_DATE"),
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
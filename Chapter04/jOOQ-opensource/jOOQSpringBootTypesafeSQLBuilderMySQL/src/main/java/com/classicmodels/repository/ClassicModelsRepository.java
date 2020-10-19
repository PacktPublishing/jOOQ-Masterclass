package com.classicmodels.repository;

import com.classicmodels.pojo.Manager;
import com.classicmodels.pojo.Order;
import com.classicmodels.pojo.CustomerAndOrder;
import java.time.LocalDate;
import java.util.List;
import static jooq.generated.tables.Customer.CUSTOMER;
import static jooq.generated.tables.Manager.MANAGER;
import static jooq.generated.tables.Order.ORDER;
import org.jooq.DSLContext;
import org.jooq.Query;
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
        
        /* Using jOOQ to build a typesafe SQL and JdbcTemplate to execute it */
        Query query = ctx.selectFrom(MANAGER) // or, ctx.select().from(MANAGER)
                .where(MANAGER.MANAGER_ID.eq(managerId));

        Manager result = (Manager) jdbcTemplate.queryForObject(query.getSQL(),
                query.getBindValues().toArray(), new BeanPropertyRowMapper(Manager.class));

        return result;
    }
    
    public List<Order> findOrdersByRequiredDate(LocalDate startDate, LocalDate endDate) {
        
        /* Using jOOQ to build the typesafe SQL and JdbcTemplate to execute it */                
        Query query = ctx.selectFrom(ORDER)                
                .where(ORDER.REQUIRED_DATE.between(startDate, endDate));

       List<Order> result = jdbcTemplate.query(query.getSQL(),
                query.getBindValues().toArray(), new BeanPropertyRowMapper(Order.class));
       
        return result;
    }            
    
    public List<CustomerAndOrder> findCustomersAndOrders() {
                
        /* Using jOOQ to build the typesafe SQL and JdbcTemplate to execute it */                
        Query query = ctx.select(CUSTOMER.CUSTOMER_NAME, ORDER.ORDER_DATE)                
                .from(ORDER)
                .innerJoin(CUSTOMER).using(CUSTOMER.CUSTOMER_NUMBER)
                .orderBy(ORDER.ORDER_DATE.desc());
       
        List<CustomerAndOrder> result = jdbcTemplate.query(query.getSQL(),
                new BeanPropertyRowMapper(CustomerAndOrder.class));

        return result;
    }        
}
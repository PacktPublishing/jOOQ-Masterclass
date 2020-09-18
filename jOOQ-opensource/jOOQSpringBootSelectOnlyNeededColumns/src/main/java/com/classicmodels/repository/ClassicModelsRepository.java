package com.classicmodels.repository;

import com.classicmodels.pojo.Order;
import static jooq.generated.tables.Order.ORDER;
import org.jooq.DSLContext;
import static org.jooq.impl.DSL.asterisk;
import org.springframework.stereotype.Repository;

@Repository
public class ClassicModelsRepository {

    private final DSLContext create;

    public ClassicModelsRepository(DSLContext create) {
        this.create = create;
    }
  
    // avoid this approach since it fetches too much data
    public Order findOrderTooMuchFields(Long orderId) {
      
        Order result = create.select() // or, selectFrom(ORDER)
                .from(ORDER)
                .where(ORDER.ORDER_ID.eq(orderId))
                .fetchOneInto(Order.class);

        return result;
    }
    
    // list only the columns that are needed
    public Order findOrderExplicitFields(Long orderId) {      
    
        Order result = create.select(ORDER.ORDER_ID, ORDER.ORDER_DATE, 
                ORDER.REQUIRED_DATE, ORDER.SHIPPED_DATE, ORDER.CUSTOMER_NUMBER)
                .from(ORDER)
                .where(ORDER.ORDER_ID.eq(orderId))
                .fetchOneInto(Order.class);

        return result;
    }
    
    // list the columns that should be skipped
    public Order findOrderAsteriskExcept(Long orderId) {      
    
        Order result = create.select(asterisk().except(ORDER.COMMENTS, ORDER.STATUS))
                .from(ORDER)
                .where(ORDER.ORDER_ID.eq(orderId))
                .fetchOneInto(Order.class);

        return result;
    }
        
}
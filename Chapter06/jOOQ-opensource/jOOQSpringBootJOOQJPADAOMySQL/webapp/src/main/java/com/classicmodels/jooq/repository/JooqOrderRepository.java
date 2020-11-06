package com.classicmodels.jooq.repository;

import java.util.List;
import jooq.generated.tables.pojos.Order;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly=true)
public interface JooqOrderRepository {
    
    public List<String> findOrderStatus();
    public Order findOrderById(Long id);
}

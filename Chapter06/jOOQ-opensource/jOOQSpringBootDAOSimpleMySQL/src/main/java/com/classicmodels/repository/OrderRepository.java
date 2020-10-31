package com.classicmodels.repository;

import java.util.List;
import jooq.generated.tables.pojos.Order;

public interface OrderRepository {
    
    public List<String> findOrderStatus();
    public Order findOrderById(Long id);
}

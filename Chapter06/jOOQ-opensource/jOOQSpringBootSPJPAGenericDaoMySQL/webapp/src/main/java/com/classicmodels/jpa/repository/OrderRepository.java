package com.classicmodels.jpa.repository;

import com.classicmodels.entity.Order;
import com.classicmodels.jooq.repository.JooqOrderRepository;
import java.util.List;
import jooq.generated.tables.daos.JooqGenOrderRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface OrderRepository extends 
        JpaRepository<Order, Long>, // Spring built-in DAO
        JooqOrderRepository,        // User-defined jOOQ DAO
        JooqGenOrderRepository      // jOOQ generated DAO
{
    
    public List<Order> findFirst5ByStatusOrderByShippedDateAsc(String status);
}

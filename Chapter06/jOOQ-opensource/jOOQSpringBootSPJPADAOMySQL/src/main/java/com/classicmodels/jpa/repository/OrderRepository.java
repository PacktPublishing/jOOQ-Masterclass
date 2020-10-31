package com.classicmodels.jpa.repository;

import com.classicmodels.entity.Order;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface OrderRepository extends JpaRepository<Order, Long>,
        com.classicmodels.jooq.repository.OrderRepository {
    
    public List<Order> findFirst5ByStatusOrderByShippedDateAsc(String status);
}

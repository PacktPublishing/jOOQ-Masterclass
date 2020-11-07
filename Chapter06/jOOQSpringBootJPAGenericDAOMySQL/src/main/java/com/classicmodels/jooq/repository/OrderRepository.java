package com.classicmodels.jooq.repository;

import java.time.LocalDate;
import java.util.List;
import jooq.generated.tables.pojos.Order;
import jooq.generated.tables.records.OrderRecord;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly=true)
public interface OrderRepository extends ClassicModelsRepository<OrderRecord, Order, Long> {
    
    public List<Order> findOrderDescByDate();
    public List<Order> findOrderBetweenDate(LocalDate sd, LocalDate ed);
}
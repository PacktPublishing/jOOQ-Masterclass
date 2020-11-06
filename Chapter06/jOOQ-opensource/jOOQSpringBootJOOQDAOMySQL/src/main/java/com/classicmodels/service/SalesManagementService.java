package com.classicmodels.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.classicmodels.repository.OrderRepositoryImpl;
import java.time.LocalDate;
import jooq.generated.tables.pojos.Order;
import com.classicmodels.repository.SaleRepositoryImpl;
import jooq.generated.tables.pojos.Sale;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SalesManagementService {

    private final SaleRepositoryImpl saleRepository;
    private final OrderRepositoryImpl orderRepository;

    public SalesManagementService(SaleRepositoryImpl saleRepository,
            OrderRepositoryImpl orderRepository) {
        this.saleRepository = saleRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional(readOnly = true)
    public List<Sale> fetchSaleByFiscalYear(int year) {

        return saleRepository.fetchByFiscalYear(year); // call jOOQ DAO
    }

    public List<Sale> fetchSaleAscGtLimit(int limit) {

        return saleRepository.findSaleAscGtLimit(limit);
    }

    @Transactional(readOnly = true)
    public List<Order> fetchOrderBetweenDate(LocalDate sd, LocalDate ed) {

        return orderRepository.fetchRangeOfOrderDate(sd, ed); // call jOOQ DAO
    }

    public List<Order> fetchOrderDescByDate() {

        return orderRepository.findOrderDescByDate();
    }
}

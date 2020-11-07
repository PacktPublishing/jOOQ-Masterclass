package com.classicmodels.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.classicmodels.repository.OrderRepository;
import java.time.LocalDate;
import jooq.generated.tables.pojos.Order;
import com.classicmodels.repository.SaleRepository;
import jooq.generated.tables.pojos.Sale;

@Service
public class SalesManagementService {

    private final SaleRepository saleRepository;
    private final OrderRepository orderRepository;

    public SalesManagementService(SaleRepository saleRepository,
            OrderRepository orderRepository) {
        this.saleRepository = saleRepository;
        this.orderRepository = orderRepository;
    }

    /* call jOOQ user-defined DAOs */
    public List<Sale> fetchSaleByFiscalYear(int year) {

        return saleRepository.findSaleByFiscalYear(year);
    }

    public List<Sale> fetchSaleAscGtLimit(int limit) {

        return saleRepository.findSaleAscGtLimit(limit);
    }

    public List<Order> fetchOrderDescByDate() {

        return orderRepository.findOrderDescByDate();
    }

    public List<Order> fetchOrderBetweenDate(LocalDate sd, LocalDate ed) {

        return orderRepository.findOrderBetweenDate(sd, ed);
    }
}
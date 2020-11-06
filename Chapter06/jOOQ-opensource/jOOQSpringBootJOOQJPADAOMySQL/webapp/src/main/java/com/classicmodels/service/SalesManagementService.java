package com.classicmodels.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.classicmodels.jpa.repository.OrderRepository;
import com.classicmodels.jpa.repository.SaleRepository;
import java.time.LocalDate;
import jooq.generated.tables.pojos.Order;
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

    /* call methods from user-defined jOOQ DAO */
    public List<Sale> fetchSaleByFiscalYear(int year) {

        return saleRepository.findSaleByFiscalYear(year);
    }

    public List<Sale> fetchSaleAscGtLimit(int limit) {

        return saleRepository.findSaleAscGtLimit(limit);
    }

    public List<String> fetchOrderStatus() {

        return orderRepository.findOrderStatus();
    }

    public Order fetchOrderById(Long id) {

        return orderRepository.findOrderById(id);
    }

    /* call methods from Spring Data JPA DAO */
    public List<com.classicmodels.entity.Sale> fetchTop10By() {

        return saleRepository.findTop10By();
    }

    public List<com.classicmodels.entity.Sale> fetchAll() {

        return saleRepository.findAll();
    }

    public List<com.classicmodels.entity.Order> fetchFirst5ByStatusOrderByShippedDateAsc(String status) {

        return orderRepository.findFirst5ByStatusOrderByShippedDateAsc(status);
    }

    /* call jOOQ generated DAO */
    public List<Sale> fetchBySaleId(Long... ids) {

        return saleRepository.fetchBySaleId(ids);
    }

    public List<Sale> fetchRangeOfSale(Double lb, Double ub) {

        return saleRepository.fetchRangeOfSale(lb, ub);
    }

    public List<Order> fetchByRequiredDate(LocalDate... values) {

        return orderRepository.fetchByRequiredDate(values);
    }
}

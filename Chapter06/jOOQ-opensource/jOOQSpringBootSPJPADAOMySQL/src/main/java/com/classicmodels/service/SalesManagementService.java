package com.classicmodels.service;

import com.classicmodels.jpa.repository.OrderRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import jooq.generated.tables.pojos.Order;
import com.classicmodels.jpa.repository.SaleRepository;
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

    public List<Sale> fetchSaleByFiscalYear(int year) {

        return saleRepository.findSaleByFiscalYear(year);
    }

    public List<Sale> fetchSaleAscGtLimit(int limit) {

        return saleRepository.findSaleAscGtLimit(limit);
    }

    public List<com.classicmodels.entity.Sale> fetchTop10By() {

        return saleRepository.findTop10By();
    }

    public List<String> fetchOrderStatus() {

        return orderRepository.findOrderStatus();
    }

    public Order fetchOrderById(Long id) {

        return orderRepository.findOrderById(id);
    }

    public List<com.classicmodels.entity.Order> fetchFirst5ByStatusOrderByShippedDateAsc(String status) {

        return orderRepository.findFirst5ByStatusOrderByShippedDateAsc(status);
    }
}

package com.classicmodels.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.classicmodels.jpa.repository.SaleRepository;
import jooq.generated.tables.pojos.Sale;

@Service
public class SalesManagementService {

    private final SaleRepository saleRepository;

    public SalesManagementService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    /* call methods from user-defined jOOQ DAO */
    public List<Sale> fetchSaleByFiscalYear(int year) {

        return saleRepository.findSaleByFiscalYear(year);
    }

    public List<Sale> fetchSaleAscGtLimit(int limit) {

        return saleRepository.findSaleAscGtLimit(limit);
    }

    /* call methods from Spring Data JPA DAO */
    public List<com.classicmodels.entity.Sale> fetchTop10By() {

        return saleRepository.findTop10By();
    }

    public List<com.classicmodels.entity.Sale> fetchAll() {

        return saleRepository.findAll();
    }

    /* call jOOQ generated DAO */
    public List<Sale> fetchBySaleId(Long... ids) {

        return saleRepository.fetchBySaleId(ids);
    }

    public List<Sale> fetchRangeOfSale(Double lb, Double ub) {

        return saleRepository.fetchRangeOfSale(lb, ub);
    }
}

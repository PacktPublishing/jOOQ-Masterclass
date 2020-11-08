package com.classicmodels.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.classicmodels.repository.SaleRepository;
import jooq.generated.tables.pojos.Sale;

@Service
public class SalesManagementService {

    private final SaleRepository saleRepository;

    public SalesManagementService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    /* call jOOQ user-defined DAO */
    public List<Sale> fetchSaleByFiscalYear(int year) {

        return saleRepository.findSaleByFiscalYear(year);
    }

    public List<Sale> fetchSaleAscGtLimit(int limit) {

        return saleRepository.findSaleAscGtLimit(limit);
    }

    /* call jOOQ user-defined generic DAO */
    public List<Sale> fetchAllSale() {

        return saleRepository.fetchAll();
    }

    public void deleteSaleById(Long id) {

        saleRepository.deleteById(id);
    }
}

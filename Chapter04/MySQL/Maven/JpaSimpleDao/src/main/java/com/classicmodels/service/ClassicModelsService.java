package com.classicmodels.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.classicmodels.jpa.repository.SaleRepository;
import jooq.generated.tables.pojos.Sale;

@Service
public class ClassicModelsService {

    private final SaleRepository saleRepository;

    public ClassicModelsService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    /* call jOOQ user-defined DAOs */
    public List<Sale> fetchSaleByFiscalYear(int year) {

        return saleRepository.findSaleByFiscalYear(year);
    }

    public List<Sale> fetchSaleAscGtLimit(int limit) {

        return saleRepository.findSaleAscGtLimit(limit);
    }

    /* call Spring Data DAOs */
    public List<com.classicmodels.entity.Sale> fetchTop10By() {

        return saleRepository.findTop10By();
    }
}

package com.classicmodels.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.classicmodels.repository.SaleRepositoryImpl;
import jooq.generated.tables.pojos.Sale;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClassicModelsService {

    private final SaleRepositoryImpl saleRepository;

    public ClassicModelsService(SaleRepositoryImpl saleRepository) {
        this.saleRepository = saleRepository;
    }

    /* call jOOQ user-defined DAOs */
    public List<Sale> fetchSaleAscGtLimit(double limit) {

        return saleRepository.findSaleAscGtLimit(limit);
    }

    /* call jOOQ generated DAOs */
    @Transactional(readOnly = true)
    public List<Sale> fetchSaleByFiscalYear(int year) {

        return saleRepository.fetchByFiscalYear(year);
    }
}

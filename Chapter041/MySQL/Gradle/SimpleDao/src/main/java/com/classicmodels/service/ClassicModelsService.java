package com.classicmodels.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.classicmodels.repository.SaleRepository;
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

    public List<Sale> fetchSaleAscGtLimit(double limit) {

        return saleRepository.findSaleAscGtLimit(limit);
    }
}

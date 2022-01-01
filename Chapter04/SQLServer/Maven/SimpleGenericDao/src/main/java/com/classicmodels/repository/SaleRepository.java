package com.classicmodels.repository;

import java.util.List;
import jooq.generated.tables.pojos.Sale;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface SaleRepository extends ClassicModelsRepository<Sale, Long> {

    public List<Sale> findSaleByFiscalYear(int year);
    public List<Sale> findSaleAscGtLimit(double limit);
}

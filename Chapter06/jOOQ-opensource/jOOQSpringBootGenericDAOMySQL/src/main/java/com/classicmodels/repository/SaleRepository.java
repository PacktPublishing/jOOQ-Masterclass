package com.classicmodels.repository;

import jooq.generated.tables.pojos.Sale;
import jooq.generated.tables.records.SaleRecord;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly=true)
public interface SaleRepository extends ClassicModelsRepository<SaleRecord, Sale, Long> {
    
}

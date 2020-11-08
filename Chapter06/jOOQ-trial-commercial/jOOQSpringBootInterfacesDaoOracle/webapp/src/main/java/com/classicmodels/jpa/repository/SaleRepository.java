package com.classicmodels.jpa.repository;

import com.classicmodels.entity.Sale;
import com.classicmodels.jooq.repository.JooqSaleRepository;
import java.util.List;
import jooq.generated.tables.daos.JooqGenSaleRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface SaleRepository extends 
        JpaRepository<Sale, Long>, // Spring built-in DAO
        JooqSaleRepository,        // User-defined jOOQ DAO
        JooqGenSaleRepository      // jOOQ generated DAO
{
    
    List<Sale> findTop10By();
}

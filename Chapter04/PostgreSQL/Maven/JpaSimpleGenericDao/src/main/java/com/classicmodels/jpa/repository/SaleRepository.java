package com.classicmodels.jpa.repository;

import com.classicmodels.entity.Sale;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface SaleRepository extends JpaRepository<Sale, Integer>, 
        com.classicmodels.jooq.repository.SaleRepository {
    
    List<Sale> findTop10By();
}

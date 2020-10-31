package com.classicmodels.jpa.repository;

import com.classicmodels.entity.Customer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface CustomerRepository extends JpaRepository<Customer, Long>, 
        com.classicmodels.jooq.repository.CustomerRepository {
    
    List<Customer> findTop10By();
}

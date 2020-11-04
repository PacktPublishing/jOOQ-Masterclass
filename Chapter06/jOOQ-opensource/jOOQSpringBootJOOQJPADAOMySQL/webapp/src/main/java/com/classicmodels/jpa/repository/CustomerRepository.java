package com.classicmodels.jpa.repository;

import com.classicmodels.entity.Customer;
import com.classicmodels.jooq.repository.JooqCustomerRepository;
import java.util.List;
import jooq.generated.tables.daos.JooqGenCustomerRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface CustomerRepository extends JpaRepository<Customer, Long>, 
        JooqCustomerRepository, JooqGenCustomerRepository {
    
    List<Customer> findTop10By();
}

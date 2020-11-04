package com.classicmodels.jooq.repository;

import java.util.List;
import jooq.generated.tables.pojos.Customer;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly=true)
public interface JooqCustomerRepository extends JooqRepository<Customer, Long> {
 
    public List<Customer> findCustomerByPhone(String phone);
    public List<Customer> findCustomersOrderedByCreditLimit();    
}

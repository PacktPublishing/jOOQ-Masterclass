package com.classicmodels.repository;

import java.util.List;
import jooq.generated.tables.pojos.Customer;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly=true)
public interface CustomerRepository {
 
    public List<Customer> findCustomerByPhone(String phone);
    public List<Customer> findCustomerOrderGtCreditLimit(int cl);    
}

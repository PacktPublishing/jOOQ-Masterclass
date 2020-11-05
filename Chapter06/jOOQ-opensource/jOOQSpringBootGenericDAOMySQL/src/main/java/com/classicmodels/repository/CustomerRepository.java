package com.classicmodels.repository;

import java.util.List;
import jooq.generated.tables.pojos.Customer;
import jooq.generated.tables.records.CustomerRecord;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly=true)
public interface CustomerRepository extends ClassicModelsRepository<CustomerRecord, Customer, Long> {
 
    public List<Customer> findCustomerByPhone(String phone);
    public List<Customer> findCustomerAscGtCreditLimit(int cl);    
}

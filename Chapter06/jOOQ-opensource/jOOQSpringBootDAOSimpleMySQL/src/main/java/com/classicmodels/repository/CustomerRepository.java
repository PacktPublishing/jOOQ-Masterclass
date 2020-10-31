package com.classicmodels.repository;

import java.util.List;
import jooq.generated.tables.pojos.Customer;

public interface CustomerRepository {
 
    public List<Customer> findCustomerByPhone(String phone);
    public List<Customer> findCustomersOrderedByCreditLimit();    
}

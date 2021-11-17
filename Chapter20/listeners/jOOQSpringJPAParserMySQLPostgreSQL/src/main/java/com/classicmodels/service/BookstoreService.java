package com.classicmodels.service;

import com.classicmodels.pojo.SimpleCustomer;
import org.springframework.stereotype.Service;
import com.classicmodels.repository.CustomerdetailRepository;
import java.util.List;

@Service
public class BookstoreService {

    private final CustomerdetailRepository customerdetailRepository;
  
    public BookstoreService(CustomerdetailRepository customerdetailRepository) {

        this.customerdetailRepository = customerdetailRepository;       
    }
    
    public void fetchCustomerNotSameAddress() {
        
        List<SimpleCustomer> customers = customerdetailRepository.fetchCustomerNotSameAddress();
        
       for(SimpleCustomer customer : customers) {
           
           System.out.println(customer.getCustomerName()+" | " + customer.getAddressLineFirst() 
                   + " | " + customer.getAddressLineSecond());
       }
    }

  
}
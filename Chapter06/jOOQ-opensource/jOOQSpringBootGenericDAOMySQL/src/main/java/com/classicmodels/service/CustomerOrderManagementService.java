package com.classicmodels.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.classicmodels.repository.CustomerRepository;
import com.classicmodels.repository.OrderRepository;
import com.classicmodels.repository.SaleRepository;
import java.time.LocalDate;
import jooq.generated.tables.pojos.Customer;
import jooq.generated.tables.pojos.Order;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerOrderManagementService {

    private final CustomerRepository customerRepository;
    private final SaleRepository saleRepository;
    private final OrderRepository orderRepository;

    public CustomerOrderManagementService(CustomerRepository customerRepository,
            OrderRepository orderRepository, SaleRepository saleRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.saleRepository=saleRepository;
    }

    public List<Customer> fetchCustomerAscGtCreditLimit(int cl) {

        return customerRepository.findCustomerAscGtCreditLimit(cl);
    }

    public List<Customer> fetchCustomerByPhone(String phone) {

        return customerRepository.findCustomerByPhone(phone);
    }

    public List<Order> fetchOrderDescByDate() {

        return orderRepository.findOrderDescByDate();
    }

    public List<Order> fetchOrderBetweenDate(LocalDate sd, LocalDate ed) {

        return orderRepository.findOrderBetweenDate(sd, ed);
    }
    
    @Transactional
    public void x() {
        
      saleRepository.deleteById(1L);
     // throw new RuntimeException("qqqqqqqqqqqqqqqqqqqqqqqqqq");
        //System.out.println("..............................................");
    }
}

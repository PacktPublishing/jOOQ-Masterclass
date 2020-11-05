package com.classicmodels.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.classicmodels.repository.CustomerRepository;
import com.classicmodels.repository.OrderRepository;
import java.time.LocalDate;
import jooq.generated.tables.pojos.Customer;
import jooq.generated.tables.pojos.Order;

@Service
public class CustomerOrderManagementService {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    public CustomerOrderManagementService(CustomerRepository customerRepository,
            OrderRepository orderRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }

    public List<Customer> fetchCustomersOrderedBy5000CreditLimit() {

        return customerRepository.findCustomersOrderedBy5000CreditLimit();
    }

    public List<Customer> fetchCustomerByPhone(String phone) {

        return customerRepository.findCustomerByPhone(phone);
    }

    public List<String> fetchOrderStatuses() {

        return orderRepository.findOrderStatuses();
    }

    public List<Order> fetchOrderByShippedDate(LocalDate date) {

        return orderRepository.findOrderByShippedDate(date);
    }
}

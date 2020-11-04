package com.classicmodels;

import com.classicmodels.service.CustomerOrderManagementService;
import java.time.LocalDate;
import java.util.List;
import jooq.generated.tables.pojos.Customer;
import jooq.generated.tables.pojos.Order;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.classicmodels", "jooq.generated.tables.daos" })
public class MainApplication {

    private final CustomerOrderManagementService customerOrderManagementService;

    public MainApplication(CustomerOrderManagementService customerOrderManagementService) {
        this.customerOrderManagementService = customerOrderManagementService;
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    public ApplicationRunner init() {
        return args -> {

            List<Customer> customers 
                    = customerOrderManagementService.fetchFirstNCustomers(5);
            System.out.println("Result: " + customers);
            
            List<Order> orders 
                    = customerOrderManagementService.fetchStatusAndOrderDate("Shipped", LocalDate.of(2005, 1, 1));
            System.out.println("Result: " + orders);
        };
    }
}

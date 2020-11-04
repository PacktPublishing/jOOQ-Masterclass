package com.classicmodels;

import com.classicmodels.service.CustomerOrderManagementService;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import java.util.List;
import jooq.generated.tables.pojos.Customer;
import jooq.generated.tables.pojos.Order;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {
    "com.classicmodels", 
    "jooq.generated.tables.daos"
})
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

            System.out.println("Fetching customers ordered by credit limit:");
            List<Customer> result1 = customerOrderManagementService.fetchCustomersOrderedByCreditLimit();
            System.out.println(result1);
            
            System.out.println("Fetching customers by phone:");
            List<Customer> result2 = customerOrderManagementService.fetchCustomerByPhone("03 9520 4555");
            System.out.println(result2);
            
            System.out.println("Fetching orders status:");
            List<String> result3 = customerOrderManagementService.fetchOrderStatus();
            System.out.println(result3);
            
            System.out.println("Fetching order by id:");
            Order result4 = customerOrderManagementService.fetchOrderById(10101L);
            System.out.println(result4);
            
            System.out.println("Fetching first 10 customers:");
            List<com.classicmodels.entity.Customer> result5 = customerOrderManagementService.fetchTop10By();
            System.out.println(result5);
            
            System.out.println("Fetching first 5 orders by status ordered by shipped date:");
            List<com.classicmodels.entity.Order> result6 
                    = customerOrderManagementService.fetchFirst5ByStatusOrderByShippedDateAsc("Shipped");
            System.out.println(result6);
            
            System.out.println("Fetching orders in range 10000-11000:");
            List<Order> result7 
                    = customerOrderManagementService.fetchOrdersInRange(10000L, 11000L);
            System.out.println(result7);
            
            System.out.println("Fetching sales reps:");
            List<Customer> result8
                    = customerOrderManagementService.fetchSalesReps(1370L);
            System.out.println(result8);
        };
    }
    
    @Bean
    public Hibernate5Module hibernate5Module() {
        return new Hibernate5Module();
    }
}

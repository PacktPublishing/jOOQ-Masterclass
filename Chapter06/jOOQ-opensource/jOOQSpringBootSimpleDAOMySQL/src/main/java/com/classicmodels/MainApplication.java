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

@SpringBootApplication
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

            System.out.println("Fetching customers having a credit limit gt 5000:");
            List<Customer> result1 = customerOrderManagementService.fetchCustomerAscGtCreditLimit(5000);
            System.out.println(result1);

            System.out.println("Fetching customers by phone:");
            List<Customer> result2 = customerOrderManagementService.fetchCustomerByPhone("03 9520 4555");
            System.out.println(result2);

            System.out.println("Fetching orders desc by date:");
            List<Order> result3 = customerOrderManagementService.fetchOrderDescByDate();
            System.out.println(result3);

            System.out.println("Fetching orders between dates:");
            List<Order> result4 = customerOrderManagementService.fetchOrderBetweenDate(
                    LocalDate.of(2003, 1, 1), LocalDate.of(2003, 12, 31));
            System.out.println(result4);
        };
    }
}

package com.classicmodels;

import com.classicmodels.service.OrderPaymentService;
import java.util.List;
import java.util.Map;
import jooq.generated.tables.pojos.Order;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "jooq.generated.tables.daos", "com.classicmodels.*" })
public class MainApplication {

    private final OrderPaymentService orderPaymentService;

    public MainApplication(OrderPaymentService orderPaymentService) {
        this.orderPaymentService = orderPaymentService;
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    public ApplicationRunner init() {
        return args -> {

            System.out.println("Sample: Fetched order with id: 10101");
            Order order = orderPaymentService.fetchOrder(10101L);
            System.out.println(order);

            System.out.println("Sample: Fetched orders between 2002-01-01 and 2004-12-31:");
            Map<Long, List<Order>> orders = orderPaymentService.fetchAndGroupOrdersByCustomerId();
            System.out.println(orders);

            System.out.println("Sample: Fetched orders of customer having ID, 362:");
            List<Order> customerOrders = orderPaymentService.fetchCustomerOrders(362L);
            System.out.println(customerOrders);
        };
    }
}

package com.classicmodels;

import com.classicmodels.pojo.Order;
import com.classicmodels.service.OrderPaymentService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
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

            System.out.println("Sample 1: Fetched order with id: 10101");
            Order order1 = orderPaymentService.fetchOrderTooMuchFields(10101L);
            System.out.println(order1);
            
            System.out.println("Sample 2: Fetched order with id: 10101");
            Order order2 = orderPaymentService.fetchOrderExplicitFields(10101L);
            System.out.println(order2);
            
            System.out.println("Sample 3: Fetched order with id: 10101");
            Order order3 = orderPaymentService.fetchOrderAsteriskExcept(10101L);
            System.out.println(order3);
        };
    }
}

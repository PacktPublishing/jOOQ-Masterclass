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
orderPaymentService.callAll();
        };
    }
}

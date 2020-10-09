package com.classicmodels;

import com.classicmodels.pojo.DelayedPayment;
import com.classicmodels.pojo.Order;
import com.classicmodels.pojo.OrderAndNextOrderDate;
import com.classicmodels.service.ClassicModelsService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MainApplication {

    private final ClassicModelsService orderPaymentService;

    public MainApplication(ClassicModelsService orderPaymentService) {
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
            
            System.out.println("Sample: Fetched order date and next order date:");
            List<OrderAndNextOrderDate> ordersDates = orderPaymentService.fetchOrderAndNextOrderDate();
            System.out.println(ordersDates);
            
            System.out.println("Sample: Fetched delayed payments between 2002-01-01 and 2004-12-31:");
            List<DelayedPayment> delayedPayments = orderPaymentService.fetchDelayedPayments(
                LocalDate.of(2002, 1, 1), LocalDate.of(2004, 12, 31));
            System.out.println(delayedPayments);
        };
    }
}

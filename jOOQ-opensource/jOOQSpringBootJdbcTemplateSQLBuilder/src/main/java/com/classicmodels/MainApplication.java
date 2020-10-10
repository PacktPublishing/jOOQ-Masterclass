package com.classicmodels;

import com.classicmodels.pojo.DelayedPayment;
import com.classicmodels.pojo.Manager;
import com.classicmodels.pojo.CustomerCachingDate;
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

            System.out.println("Example: Fetched manager with id: 1");
            Manager manager = orderPaymentService.fetchManager(1L);
            System.out.println(manager);
            
            System.out.println("Example: Fetched caching date and next caching date:");
            List<CustomerCachingDate> cachingDates = orderPaymentService.fetchCustomerCachingDate();
            System.out.println(cachingDates);
            
            System.out.println("Example: Fetched delayed payments between 2002-01-01 and 2004-12-31:");
            List<DelayedPayment> delayedPayments = orderPaymentService.fetchDelayedPayments(
                LocalDate.of(2002, 1, 1), LocalDate.of(2004, 12, 31));
            System.out.println(delayedPayments);                        
        };
    }
}
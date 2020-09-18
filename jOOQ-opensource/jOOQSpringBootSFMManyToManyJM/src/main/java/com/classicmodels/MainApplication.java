package com.classicmodels;

import com.classicmodels.pojo.ManagerDTO;
import com.classicmodels.pojo.OfficeDTO;
import com.classicmodels.service.CustomerService;
import java.util.List;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MainApplication {

    private final CustomerService customerRepository;

    public MainApplication(CustomerService customerRepository) {
        this.customerRepository = customerRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    public ApplicationRunner init() {
        return args -> {

            System.out.println("Sample: Fetch customers having a credit limit smaller than 50000:");
            List<ManagerDTO> customers = customerRepository.fetchCustomerByCreditLimit(50000.0f);
            System.out.println(customers);
                       
        };
    }
}

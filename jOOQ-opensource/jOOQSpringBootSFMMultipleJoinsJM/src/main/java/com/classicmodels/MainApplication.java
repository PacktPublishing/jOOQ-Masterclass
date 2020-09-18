package com.classicmodels;

import com.classicmodels.pojo.EmployeeDTO;
import com.classicmodels.service.EmployeeService;
import java.util.List;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MainApplication {

    private final EmployeeService employeeService;

    public MainApplication(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    public ApplicationRunner init() {
        return args -> {

            System.out.println("Sample: Fetch employees from a certain office (4) with customers and sales:");
            List<EmployeeDTO> employees = employeeService.fetchEmployeeWithSalesAndCustomersByOfficeCode("4");
            System.out.println(employees);
        };
    }
}

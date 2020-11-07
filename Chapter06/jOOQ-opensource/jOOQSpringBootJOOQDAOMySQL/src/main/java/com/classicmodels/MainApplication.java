package com.classicmodels;

import com.classicmodels.service.SalesManagementService;
import java.time.LocalDate;
import java.util.List;
import jooq.generated.tables.pojos.Order;
import jooq.generated.tables.pojos.Sale;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MainApplication {

    private final SalesManagementService salesManagementService;

    public MainApplication(SalesManagementService salesManagementService) {
        this.salesManagementService = salesManagementService;
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    public ApplicationRunner init() {
        return args -> {

            /* call jOOQ user-defined DAOs */
            System.out.println("Fetching sales over 5000:");
            List<Sale> result1 = salesManagementService.fetchSaleAscGtLimit(5000);
            System.out.println(result1);
            
            System.out.println("Fetching orders desc by date:");
            List<Order> result2 = salesManagementService.fetchOrderDescByDate();
            System.out.println(result2);
            
            /* call jOOQ generated DAOs */
            System.out.println("Fetching sales in 2003:");
            List<Sale> result3 = salesManagementService.fetchSaleByFiscalYear(2003);
            System.out.println(result3);

            System.out.println("Fetching orders between dates, 2003-01-01 and 2003-12-31:");
            List<Order> result4 = salesManagementService.fetchOrderBetweenDate(
                    LocalDate.of(2003, 1, 1), LocalDate.of(2003, 12, 31));
            System.out.println(result4);
        };
    }
}

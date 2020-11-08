package com.classicmodels;

import com.classicmodels.service.SalesManagementService;
import java.util.List;
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

            /* call jOOQ user-defined DAO */
            System.out.println("Fetching sales over 5000:");
            List<Sale> result1 = salesManagementService.fetchSaleAscGtLimit(5000);
            System.out.println(result1);

            System.out.println("Fetching sales in 2003:");
            List<Sale> result2 = salesManagementService.fetchSaleByFiscalYear(2003);
            System.out.println(result2);

            /* call jOOQ user-defined generic DAO */
            System.out.println("Fetching all orders:");
            List<Sale> result5 = salesManagementService.fetchAllSale();
            System.out.println(result5);

            System.out.println("Delete sale with id 1:");
            salesManagementService.deleteSaleById(1L);
        };
    }
}

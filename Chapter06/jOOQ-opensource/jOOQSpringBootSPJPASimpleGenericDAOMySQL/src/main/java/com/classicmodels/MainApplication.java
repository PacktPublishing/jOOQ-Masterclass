package com.classicmodels;

import com.classicmodels.service.SalesManagementService;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
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

            System.out.println("Fetching sales from 2003:");
            List<Sale> result1 = salesManagementService.fetchSaleByFiscalYear(2003);
            System.out.println(result1);

            System.out.println("Fetching sales (asc) greater than limit:");
            List<Sale> result2 = salesManagementService.fetchSaleAscGtLimit(5000);
            System.out.println(result2);

            System.out.println("Fetching top 10 sales:");
            List<com.classicmodels.entity.Sale> result3 = salesManagementService.fetchTop10By();
            System.out.println(result3);

            System.out.println("Fetching all sales:");
            List<Sale> result4 = salesManagementService.fetchAllSales();
            System.out.println(result4);

            System.out.println("Fetching orders desc by date:");
            List<Order> result5 = salesManagementService.fetchOrderDescByDate();
            System.out.println(result5);

            System.out.println("Fetching orders between dates:");
            List<Order> result6 = salesManagementService.fetchOrderBetweenDate(
                    LocalDate.of(2003, 1, 1), LocalDate.of(2003, 12, 31));
            System.out.println(result6);

            System.out.println("Fetching first 5 orders by status ordered by shipped date:");
            List<com.classicmodels.entity.Order> result7
                    = salesManagementService.fetchFirst5ByStatusOrderByShippedDateAsc("Shipped");
            System.out.println(result7);

            System.out.println("Fetching all orders:");
            List<Order> result8 = salesManagementService.fetchAllOrders();
            System.out.println(result8);
        };
    }

    @Bean
    public Hibernate5Module hibernate5Module() {
        return new Hibernate5Module();
    }
}

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
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {
    "com.classicmodels",
    "jooq.generated.tables.daos"
})
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

            /* call methods from user-defined jOOQ DAO */
            System.out.println("Fetching sales by fiscal year, 2003:");
            List<Sale> result1 = salesManagementService.fetchSaleByFiscalYear(2003);
            System.out.println(result1);

            System.out.println("Fetching sales gt limit, 5000:");
            List<Sale> result2 = salesManagementService.fetchSaleAscGtLimit(5000);
            System.out.println(result2);

            System.out.println("Fetching statuses of orders:");
            List<String> result3 = salesManagementService.fetchOrderStatus();
            System.out.println(result3);

            System.out.println("Fetching order by id, 10100:");
            Order result4 = salesManagementService.fetchOrderById(10100L);
            System.out.println(result4);

            /* call methods from Spring Data JPA DAO */
            System.out.println("Fetching top 10 sales:");
            List<com.classicmodels.entity.Sale> result5 = salesManagementService.fetchTop10By();
            System.out.println(result5);

            System.out.println("Fetching all sales:");
            List<com.classicmodels.entity.Sale> result6 = salesManagementService.fetchAll();
            System.out.println(result6);

            System.out.println("Fetching first 5 orders by status, 'Shipped':");
            List<com.classicmodels.entity.Order> result7 = salesManagementService
                    .fetchFirst5ByStatusOrderByShippedDateAsc("Shipped");
            System.out.println(result7);

            /* call jOOQ generated DAO */
            System.out.println("Fetching sale by ids, 1-5:");
            List<Sale> result8 = salesManagementService.fetchBySaleId(1L, 2L, 3L, 4L, 5L);
            System.out.println(result8);

            System.out.println("Fetching range of sales, 2000-5000:");
            List<Sale> result9 = salesManagementService.fetchRangeOfSale(2000.0, 5000.0);
            System.out.println(result9);

            System.out.println("Fetching order by required dates, 2003-01-13; 2003-01-18:");
            List<Order> result10 = salesManagementService.fetchByRequiredDate(
                    LocalDate.of(2003, 1, 13), LocalDate.of(2003, 1, 18));
            System.out.println(result10);
        };
    }

    @Bean
    public Hibernate5Module hibernate5Module() {
        return new Hibernate5Module();
    }
}

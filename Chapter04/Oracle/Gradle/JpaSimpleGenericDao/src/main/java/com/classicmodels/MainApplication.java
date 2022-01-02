package com.classicmodels;

import com.classicmodels.service.ClassicModelsService;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import java.util.List;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = {R2dbcAutoConfiguration.class})
public class MainApplication {

    private final ClassicModelsService salesManagementService;

    public MainApplication(ClassicModelsService salesManagementService) {
        this.salesManagementService = salesManagementService;
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    public ApplicationRunner init() {
        return args -> {

            /* call jOOQ user-defined DAOs */
            System.out.println("Fetching sales from 2003:");
            List<jooq.generated.tables.pojos.Sale> result1 = salesManagementService.fetchSaleByFiscalYear(2003);
            System.out.println(result1);

            System.out.println("Fetching sales (asc) greater than limit, 5000:");
            List<jooq.generated.tables.pojos.Sale> result2 = salesManagementService.fetchSaleAscGtLimit(5000);
            System.out.println(result2);

            /* call Spring Data JPA DAOs */
            System.out.println("Fetching top 10 sales:");
            List<com.classicmodels.entity.Sale> result3 = salesManagementService.fetchTop10By();
            System.out.println(result3);

            /* call jOOQ user-defined generic DAOs */
            System.out.println("Fetching all sales:");
            List<jooq.generated.tables.pojos.Sale> result4 = salesManagementService.fetchAllSales();
            System.out.println(result4);
            
            System.out.println("Delete sale with id 1:");
            salesManagementService.deleteSaleById(1L);
        };
    }

    @Bean
    public Hibernate5Module hibernate5Module() {
        return new Hibernate5Module();
    }
}

package com.classicmodels;

import com.classicmodels.service.SalesManagementService;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import java.util.List;
import jooq.generated.tables.pojos.Sale;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = { R2dbcAutoConfiguration.class })
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

            /* call methods from Spring Data JPA DAO */
            System.out.println("Fetching top 10 sales:");
            List<com.classicmodels.entity.Sale> result3 = salesManagementService.fetchTop10By();
            System.out.println(result3);

            System.out.println("Fetching all sales:");
            List<com.classicmodels.entity.Sale> result4 = salesManagementService.fetchAll();
            System.out.println(result4);
            
            /* call jOOQ generated DAO */
            System.out.println("Fetching sale by ids, 1 and 2:");
            List<Sale> result5 = salesManagementService.fetchBySaleId(1L, 2L);
            System.out.println(result5);

            System.out.println("Fetching range of sales, 2000-5000:");
            List<Sale> result6 = salesManagementService.fetchRangeOfSale(2000.0, 5000.0);
            System.out.println(result6);          
        };
    }

    @Bean
    public Hibernate5Module hibernate5Module() {
        return new Hibernate5Module();
    }
}

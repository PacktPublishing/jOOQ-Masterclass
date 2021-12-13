package com.classicmodels;

import com.classicmodels.pojo.CustomerAndOrder;
import com.classicmodels.service.ClassicModelsService;
import java.time.LocalDate;
import java.util.List;
import jooq.generated.tables.pojos.JooqOffice;
import jooq.generated.tables.pojos.JooqOrder;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = {R2dbcAutoConfiguration.class})
public class MainApplication {

    private final ClassicModelsService classicModelsService;

    public MainApplication(ClassicModelsService classicModelsService) {
        this.classicModelsService = classicModelsService;
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    public ApplicationRunner init() {
        return args -> {

            System.out.println("Fetching offices from 'NA' territory:");
            List<JooqOffice> offices = classicModelsService.fetchOfficesInTerritory("NA");
            System.out.println(offices);

            System.out.println("Fetching orders between 2002-01-01 and 2004-12-31:");
            List<JooqOrder> orders = classicModelsService
                    .fetchOrdersByRequiredDate(LocalDate.of(2002, 1, 1), LocalDate.of(2004, 12, 31));
            System.out.println(orders);

            System.out.println("Fetching customers and orders:");
            List<CustomerAndOrder> custAndOrd = classicModelsService.fetchCustomersAndOrders();
            System.out.println(custAndOrd);
        };
    }
}

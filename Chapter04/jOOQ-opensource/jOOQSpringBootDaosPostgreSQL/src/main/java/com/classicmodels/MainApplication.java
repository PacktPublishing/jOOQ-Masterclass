package com.classicmodels;

import com.classicmodels.pojo.CustomerAndOrder;
import com.classicmodels.service.ClassicModelsService;
import java.time.LocalDate;
import java.util.List;
import jooq.generated.tables.pojos.JooqManager;
import jooq.generated.tables.pojos.JooqOrder;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "jooq.generated.tables.daos", "com.classicmodels.*" })
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

            System.out.println("Example: Fetched manager with id: 1");
            JooqManager manager = classicModelsService.fetchManager(1L);
            System.out.println(manager);

            System.out.println("Example: Fetched orders between 2002-01-01 and 2004-12-31:");
            List<JooqOrder> orders = classicModelsService
                    .fetchOrdersByRequiredDate(LocalDate.of(2002, 1, 1), LocalDate.of(2004, 12, 31));
            System.out.println(orders);

            System.out.println("Example: Fetched customers and orders:");
            List<CustomerAndOrder> custAndOrd = classicModelsService.fetchCustomersAndOrders();
            System.out.println(custAndOrd);
        };
    }
}

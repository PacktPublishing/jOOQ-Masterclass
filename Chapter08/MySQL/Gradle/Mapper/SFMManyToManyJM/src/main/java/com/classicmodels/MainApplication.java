package com.classicmodels;

import com.classicmodels.pojo.SimpleManager;
import com.classicmodels.pojo.SimpleOffice;
import com.classicmodels.service.ClasicModelsService;
import java.util.List;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = {R2dbcAutoConfiguration.class})
public class MainApplication {

    private final ClasicModelsService classicModelsService;

    public MainApplication(ClasicModelsService classicModelsService) {
        this.classicModelsService = classicModelsService;
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    public ApplicationRunner init() {
        return args -> {

            System.out.println("Sample: Fetch managers and offices:");
            List<SimpleManager> result1 = classicModelsService.fetchManagerAndOffice();
            for (SimpleManager manager : result1) {
                System.out.println("==================================");
                System.out.println(manager);
                System.out.println(manager.getOffices());
            }

            System.out.println("Sample: Fetch offices and managers:");
            List<SimpleOffice> result2 = classicModelsService.fetchOfficeAndManager();
            for (SimpleOffice office : result2) {
                System.out.println("==================================");
                System.out.println(office);
                System.out.println(office.getManagers());
            }
        };
    }
}

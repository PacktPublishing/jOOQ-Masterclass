package com.rsvp.hml;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MainApplication {   

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    public ApplicationRunner initializeConnection(HmlHandler hmlHandler,
            RsvpsWebSocketHandler rsvpsWebSocketHandler) {
        return args -> {
          hmlHandler.recoverAfterRestart();
          rsvpsWebSocketHandler.doHandshake();          
        };                
    }
}
package com.vehiclerental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VehicleRentalApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(VehicleRentalApplication.class, args);
        System.out.println("========================================");
        System.out.println("Vehicle Rental Platform Started!");
        System.out.println("API Documentation: http://localhost:8080/swagger-ui.html");
        System.out.println("========================================");
    }
}

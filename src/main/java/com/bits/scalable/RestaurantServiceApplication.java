package com.bits.scalable;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestaurantServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantServiceApplication.class, args);
        System.out.println("Restaurant Service is running...");
        System.out.println("Swagger UI available at: http://localhost:8088/swagger-ui.html");
    }
}

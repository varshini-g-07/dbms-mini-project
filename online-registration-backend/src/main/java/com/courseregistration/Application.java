package com.courseregistration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * The main entry point for the Spring Boot application.
 * This class handles bootstrapping, component scanning, and starting the embedded server.
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.courseregistration") 
public class Application {

    public static void main(String[] args) {
        // This static method runs the Spring application
        SpringApplication.run(Application.class, args);
        
        // Once this runs, the application will be accessible at http://localhost:8080/
        System.out.println("\n---------------------------------------------------------");
        System.out.println("Spring Boot Application started successfully!");
        System.out.println("Access API endpoints at http://localhost:8080/api/v1/");
        System.out.println("---------------------------------------------------------\n");
    }
}

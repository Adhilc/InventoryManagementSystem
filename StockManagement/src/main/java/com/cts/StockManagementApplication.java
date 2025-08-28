package com.cts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

//The main entry point for the Stock Management Spring Boot application.
 
@SpringBootApplication // A convenience annotation that enables auto-configuration and component scanning.
@EnableFeignClients // Enables the use of Feign for creating declarative REST API clients.
public class StockManagementApplication {


	public static void main(String[] args) {
		SpringApplication.run(StockManagementApplication.class, args);
	}

}
package com.cts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * The main entry point for the Product Management Spring Boot application.
 * <p>
 * The {@code @SpringBootApplication} annotation is a convenience annotation
 * that adds all of the following:
 * <ul>
 * <li>{@code @Configuration}: Tags the class as a source of bean definitions
 * for the application context.</li>
 * <li>{@code @EnableAutoConfiguration}: Tells Spring Boot to start adding beans
 * based on class path settings, other beans, and various property
 * settings.</li>
 * <li>{@code @ComponentScan}: Tells Spring to look for other components,
 * configurations, and services in the 'com.cts' package, allowing it to find
 * and register them.</li>
 * </ul>
 */
@SpringBootApplication
@EnableFeignClients
public class ProductManagementApplication {

	/**
	 * The main method, which serves as the entry point for the application. It
	 * delegates to Spring Boot's {@link SpringApplication} class by calling the
	 * static {@code run} method to bootstrap the application.
	 *
	 * @param args Command-line arguments passed to the application.
	 */
	public static void main(String[] args) {
		// Launches the Spring application
		SpringApplication.run(ProductManagementApplication.class, args);
	}

}
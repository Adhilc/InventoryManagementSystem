package com.cts.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Declares a Feign client for communicating with a remote microservice.
 * Spring Cloud will automatically create a proxy implementation for this interface.
 *
 * @FeignClient specifies the details of the target service.
 * value="PRODUCTMANAGEMENT": This is the logical name (Service ID) of the target microservice.
 * Feign will use a discovery client (like Eureka) to look up the actual
 * host and port for the "PRODUCTMANAGEMENT" service.
 * path="/api/product": This is a base path that will be prepended to all request paths defined in this interface.
 */
@FeignClient(value="PRODUCTMANAGEMENT",path="/api/product")
public interface ProductManagementClient {

	/**
	 * Defines a method that maps to a remote REST endpoint.
	 * When this method is called, Feign will send an HTTP POST request.
	 * The full path of the request will be: /api/product/checkProductId
	 *
	 * @param id The product ID to be checked.
	 * @RequestParam("id") maps the 'id' method parameter to an HTTP request parameter named "id".
	 * For a POST request, this would typically be sent in the request body as form data.
	 * @return An integer value returned from the remote service.
	 */
	@PostMapping("/checkProductId")
	int checkProductId(@RequestParam("id") int id);
}
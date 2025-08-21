package com.cts.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.cts.model.ProductDTO;
import com.cts.model.Stock;

/**
 * Declares a Feign client to communicate with the 'STOCKMANAGEMENT' microservice.
 * Spring Cloud will automatically create a proxy implementation for this interface at runtime.
 *
 * @FeignClient specifies the details of the target service.
 * value="STOCKMANAGEMENT": This is the logical name (Service ID) of the target microservice.
 * Feign will use a discovery client (like Eureka) to find the network location
 * for the "STOCKMANAGEMENT" service.
 * path="/api/stock": This is a base path that will be prepended to all request paths defined in this interface.
 */
@FeignClient(value="STOCKMANAGEMENT",path="/api/stock")
public interface StockManagementClient {

	/**
	 * Defines a method that maps to a remote REST endpoint to decrease product stock.
	 * When this method is called, Feign will send an HTTP POST request to the full path: /api/stock/decrease
	 *
	 * @param productDto The object containing product details (like ID and quantity) needed to decrease the stock.
	 * @RequestBody tells Feign to serialize the 'productDto' object into JSON and place it in the body of the POST request.
	 * @return A ResponseEntity containing the full HTTP response from the remote service. This allows for checking
	 * the HTTP status code and headers. The body of the response is expected to be a 'Stock' object, likely
	 * representing the updated stock information.
	 */
	@PostMapping("/decrease")
	ResponseEntity<Stock> decreaseStockFromOrder(@RequestBody ProductDTO productDto);
	
}
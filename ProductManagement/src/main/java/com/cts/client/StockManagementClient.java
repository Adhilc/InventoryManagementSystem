package com.cts.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.cts.model.StockDTO;

/**
 * Feign client for the STOCKMANAGEMENT microservice.
 * <p>
 * This interface defines the contract for communicating with the
 * STOCKMANAGEMENT service. The {@code @FeignClient} annotation specifies the
 * service name ("STOCKMANAGEMENT") and the base path for its endpoints
 * ("/api/stock"). Feign automatically creates an implementation of this interface
 * at runtime, handling the HTTP requests.
 * </p>
 */
@FeignClient(name = "STOCKMANAGEMENT",path="/api/stock")
public interface StockManagementClient {

	@PostMapping("/save") 
	void saveStock(@RequestBody StockDTO stock);
	/**
	 * Sends a POST request to the "/save" endpoint of the STOCKMANAGEMENT service.
	 * <p>
	 * This method is used to save a new stock entry in the remote service.
	 * The {@code @RequestBody} annotation indicates that the {@code stockDto}
	 * object should be serialized into the body of the HTTP request.
	 * </p>
	 *
	 * @param stockDto The data transfer object containing the stock information to be saved.
	 */

}
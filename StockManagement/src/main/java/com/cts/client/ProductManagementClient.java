package com.cts.client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.cts.stockmanagementmodel.QuantityDTO;
// Defines a Feign client for the service registered as "PRODUCTMANAGEMENT" with a base API path.
@FeignClient(name="PRODUCTMANAGEMENT" , path="/api/product")
public interface ProductManagementClient {
	/**
	 * Calls the PUT endpoint on the Product Management service to update a product's quantity.
	 * @param quantityDTO A DTO containing the product ID and the new quantity, sent as the request body.
	 * @return A confirmation message from the product management service.
	 */
	@PutMapping("/updateQuantity")
	String updateQuantity(@RequestBody QuantityDTO quantityDTO);
	
}
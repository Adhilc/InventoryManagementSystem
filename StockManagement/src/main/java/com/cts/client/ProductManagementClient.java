package com.cts.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.cts.stockmanagementmodel.QuantityDTO;

@FeignClient(name="PRODUCTMANAGEMENT" , path="/api/product")
public interface ProductManagementClient {

	
	@PutMapping("/updateQuantity")
	String updateQuantity(@RequestBody QuantityDTO quantityDTO);
	
}

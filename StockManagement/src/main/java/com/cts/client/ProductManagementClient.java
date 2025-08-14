package com.cts.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.cts.stockmanagementmodel.OverAllStock;
import com.cts.stockmanagementmodel.QuantityDTO;

@FeignClient(value="PRODUCTMANAGEMENT" , path="/api/product")
public interface ProductManagementClient {
	
	@GetMapping("/getAll")
	List<OverAllStock> getAllProductsStocks();
	
	@PutMapping("/updateQuantity")
	String updateQuantity(QuantityDTO quantityDTO);
	
}

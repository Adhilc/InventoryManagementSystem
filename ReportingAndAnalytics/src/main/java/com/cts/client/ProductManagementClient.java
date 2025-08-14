package com.cts.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.cts.model.OverAllStock;

@FeignClient(value="PRODUCTMANAGEMENT",path="/api/product")
public interface ProductManagementClient {
	@GetMapping("/getAll")
	List<OverAllStock> getAllProductsStocks();

}

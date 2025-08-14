package com.cts.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;

import com.cts.model.ProductDTO;
import com.cts.model.Stock;

@FeignClient(value="STOCKMANAGEMENT",path="/api/stock")
public interface StockManagementClient {

	@PutMapping("/decrease")
	ResponseEntity<Stock> decreaseStockFromOrder(ProductDTO productDto);
	
}

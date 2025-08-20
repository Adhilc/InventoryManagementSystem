package com.cts.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.cts.model.StockDTO;

@FeignClient(name = "STOCKMANAGEMENT",path="/api/stock")
public interface StockManagementClient {

	@PostMapping("/save") 
	void save(@RequestBody StockDTO stockDto);
}
package com.cts.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.cts.model.StockDTO;

@FeignClient(value="STOCKMANAGEMENT",path="/api/stock")
public interface StockManagementClient {
	
	@GetMapping("/send-stock-report")
	ResponseEntity<List<StockDTO>> getLowStockReport();

}

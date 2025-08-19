package com.cts.client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.cts.model.StockDTO;

@FeignClient(name = "STOCKMANAGEMENTSERVICE")
public interface StockManagementClient {

    @PostMapping("/stocks")
	static
    void createStock(@RequestBody StockDTO stockDto) {		
	}
}
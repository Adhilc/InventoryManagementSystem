package com.cts.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value="PRODUCTMANAGEMENT",path="/api/product")
public interface ProductManagementClient {

	@PostMapping("/checkProductId")
	int checkProductId(@RequestParam("id") int id);
}

package com.cts.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value="PRODUCTMANAGEMENT",path="/api/product")
public interface ProductManagementClient {

	@GetMapping("/checkProductId")
	int checkProductId(int id);
}

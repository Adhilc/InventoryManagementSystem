package com.cts.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.cts.model.OrderReport;
import com.cts.model.OrderReportSent;

@FeignClient(value="ORDERMANAGEMENT", path="/api/order/")
public interface OrderManagementClient {

	@GetMapping("/getByDate")
	ResponseEntity<List<OrderReportSent>> getDetailsByDate(OrderReport orderReport);
}

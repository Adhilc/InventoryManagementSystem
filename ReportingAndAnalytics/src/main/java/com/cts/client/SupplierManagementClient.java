package com.cts.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.cts.model.SupplierReport;
import com.cts.model.SupplierReportSent;

@FeignClient(value="SUPPLIERMANAGEMENT",path="/api/supplier")
public interface SupplierManagementClient {

	@PostMapping("/supplierInfoByDateForReport")
	ResponseEntity<List<SupplierReportSent>> getSupplierInfoForReport(@RequestBody SupplierReport report);
}

package com.cts.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.cts.model.SupplierReport;
import com.cts.model.SupplierReportSent;

@FeignClient(value="SUPPLIERMANAGEMENT",path="/api/supplier/")
public interface SupplierManagementClient {

	@GetMapping("supplierInfoByDateForReport")
	List<SupplierReportSent>getSupplierInfoForReport(SupplierReport report);
}

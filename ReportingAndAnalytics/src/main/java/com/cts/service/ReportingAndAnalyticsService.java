package com.cts.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.cts.model.OrderReport;
import com.cts.model.OrderReportSent;
import com.cts.model.OverAllStock;
import com.cts.model.StockDTO;
import com.cts.model.SupplierReport;
import com.cts.model.SupplierReportSent;

public interface ReportingAndAnalyticsService {

	ResponseEntity<List<OrderReportSent>> getDetailsByDate(OrderReport orderReport);

	List<SupplierReportSent> getSupplierDetailsByDate(SupplierReport supplierReport);

	ResponseEntity<List<StockDTO>> getTheLowerStocks();

	List<OverAllStock> getAllStocks();
	
}

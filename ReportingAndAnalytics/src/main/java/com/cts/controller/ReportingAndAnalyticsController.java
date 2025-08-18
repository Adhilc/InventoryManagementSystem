package com.cts.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.model.OrderReport;
import com.cts.model.OrderReportSent;
import com.cts.model.OverAllStock;
import com.cts.model.StockDTO;
import com.cts.model.SupplierReport;
import com.cts.model.SupplierReportSent;
import com.cts.service.ReportingAndAnalyticsService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/report")
@Slf4j
public class ReportingAndAnalyticsController {

	private ReportingAndAnalyticsService service;
	
	public ReportingAndAnalyticsController(ReportingAndAnalyticsService service) {
		this.service=service;
	}
	@GetMapping("/getByDate/order/{startDate}/{endDate}")
	public ResponseEntity<List<OrderReportSent>> getOrderDetailsByDate(@PathVariable LocalDateTime startDate,@PathVariable LocalDateTime endDate){
			
		OrderReport orderReport = new OrderReport();
		orderReport.setStartDate(startDate);
		orderReport.setEndDate(endDate);
		return service.getDetailsByDate(orderReport);
	}
	@GetMapping("/getByDate/supplier/{startDate}/{endDate}")
	public ResponseEntity<List<SupplierReportSent>> getSupplierDetailsByDate(@PathVariable LocalDateTime startDate,@PathVariable LocalDateTime endDate){
			log.info("Start Date: "+startDate+" End Date :"+endDate);
		SupplierReport supplierReport=new SupplierReport();
		supplierReport.setStartDate(startDate);
		supplierReport.setEndDate(endDate);
		return service.getSupplierDetailsByDate(supplierReport);
	}
	
	@GetMapping("/getTheLowerStocks")
	public ResponseEntity<List<StockDTO>> getTheLowerStocks() {
		
		return service.getTheLowerStocks();
	}
	
	@GetMapping("/getAllStocks")
	public List<OverAllStock> getAllStocks(){
		
		return service.getAllStocks();
	}
}

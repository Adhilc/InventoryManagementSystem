package com.cts.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cts.client.OrderManagementClient;
import com.cts.client.ProductManagementClient;
import com.cts.client.StockManagementClient;
import com.cts.client.SupplierManagementClient;
import com.cts.model.OrderReport;
import com.cts.model.OrderReportSent;
import com.cts.model.OverAllStock;
import com.cts.model.StockDTO;
import com.cts.model.SupplierReport;
import com.cts.model.SupplierReportSent;

@Service
public abstract class ReportingAndAnalyticsServiceImpl implements ReportingAndAnalyticsService  {

	private OrderManagementClient oClient;
	private SupplierManagementClient sClient;
	private StockManagementClient stClient;
	private ProductManagementClient pClient;
	public ReportingAndAnalyticsServiceImpl(OrderManagementClient oClient,SupplierManagementClient sClient,StockManagementClient stClient,ProductManagementClient pClient) {
		this.oClient=oClient;
		this.sClient=sClient;
		this.stClient=stClient;
		this.pClient=pClient;
	}
	@Override
	public ResponseEntity<List<OrderReportSent>> getDetailsByDate(OrderReport orderReport) {
		
		ResponseEntity<List<OrderReportSent>> response=oClient.getDetailsByDate(orderReport);
		return response;
	}
	@Override
	public List<SupplierReportSent> getSupplierDetailsByDate(SupplierReport supplierReport) {
		
		List<SupplierReportSent> response=sClient.getSupplierInfoForReport(supplierReport);
		return response;
	}
	@Override
	public ResponseEntity<List<StockDTO>> getTheLowerStocks() {
		
		ResponseEntity<List<StockDTO>> response= stClient.getLowStockReport();
		return response;
	}

	@Override
	public List<OverAllStock> getAllStocks() {
		
		List<OverAllStock> response = pClient.getAllProductsStocks();
		
		return response;
	}

}

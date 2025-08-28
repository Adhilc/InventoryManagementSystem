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

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReportingAndAnalyticsServiceImpl implements ReportingAndAnalyticsService {

	private OrderManagementClient oClient;
	private SupplierManagementClient sClient;
	private StockManagementClient stClient;
	private ProductManagementClient pClient;
	
	public ReportingAndAnalyticsServiceImpl(OrderManagementClient oClient, SupplierManagementClient sClient,
			StockManagementClient stClient, ProductManagementClient pClient) {
		this.oClient = oClient;
		this.sClient = sClient;
		this.stClient = stClient;
		this.pClient = pClient;
	}

	@Override
	public ResponseEntity<List<OrderReportSent>> getDetailsByDate(OrderReport orderReport) {
         log.info("Fetching order details for date range:{}",orderReport);
		return oClient.getDetailsByDate(orderReport);
	}

	@Override
	public ResponseEntity<List<SupplierReportSent>> getSupplierDetailsByDate(SupplierReport supplierReport) {
		log.info("Report Object:" + supplierReport);

		return sClient.getSupplierInfoForReport(supplierReport);
	}

	@Override
	public ResponseEntity<List<StockDTO>> getTheLowerStocks() {

		log.info("Fetching low stock report from StockManagementClient");
	 
		return stClient.getLowStockReport();
	}

	@Override
	public List<OverAllStock> getAllStocks() {

		log.info("Fetching overall stock information from ProductManagementClient");

		return pClient.getAllProductsStocks();
	}

}

package com.cts.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.cts.exception.DataNotFoundException;
import com.cts.exception.DateNotFoundException;
import com.cts.exception.OrderNotFoundException;
import com.cts.model.Order;
import com.cts.model.OrderReport;
import com.cts.model.OrderReportSent;
import com.cts.model.Product;
import com.cts.model.ProductDTO;

public interface OrderManagementService {
	
	ResponseEntity<String> createOrder(Product product) throws DataNotFoundException;

	ResponseEntity<List<Order>> getDetailsByCustomerId(int id) throws OrderNotFoundException;

	ResponseEntity<Order> getDetailsByOrderId(int id) throws OrderNotFoundException;

	ResponseEntity<List<OrderReportSent>> getDetailsByDate(OrderReport orderReport) throws DateNotFoundException;

	String updateStatus(int orderId, String status) throws OrderNotFoundException;
}
package com.cts.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.exception.DataNotFoundException;
import com.cts.exception.DateNotFoundException;
import com.cts.exception.OrderNotFoundException;
import com.cts.model.Order;
import com.cts.model.OrderReport;
import com.cts.model.OrderReportSent;
import com.cts.model.Product;
import com.cts.service.OrderManagementService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/order")
public class OrderManagementController {

	private OrderManagementService service;
	public OrderManagementController(OrderManagementService service) {
		this.service=service;
	}
	
	/**
 	 * Endpoint for creating a new order.
 	 * It accepts a `Product` object in the request body and delegates the creation to the service layer.
 	 * @param product The product details to be ordered.
 	 * @return A `ResponseEntity` with a confirmation message.
 	 * @throws DataNotFoundException if the input product data is invalid.
 	 */
	
	@PostMapping("/save")
	public ResponseEntity<String> createOrder(@RequestBody Product product) throws DataNotFoundException{
		
		log.info("In the OrderMangementController we are creating order");
		return service.createOrder(product);
	}
	
	/**
 	 * Endpoint to retrieve a specific order by its unique ID.
 	 * @param id The order ID, passed as a path variable.
 	 * @return A `ResponseEntity` containing the found `Order` object.
 	 * @throws OrderNotFoundException if no order with the given ID exists.
 	 */
	@GetMapping("/getByOrderId/{id}")
	public ResponseEntity<Order> getDetailsByOrderId(@PathVariable("id") int id) throws OrderNotFoundException{
		
		log.info("In the OrderMangementController we are retriving order by order Id");
		
		return service.getDetailsByOrderId(id);
	}
	
	/**
 	 * Endpoint to get all orders placed by a specific customer.
 	 * @param id The customer ID, passed as a path variable.
 	 * @return A `ResponseEntity` with a `List` of `Order` objects.
 	 * @throws OrderNotFoundException if no orders are found for the customer.
 	 */
	
	@GetMapping("getByCustomerId/{id}")
	public ResponseEntity<List<Order>>getDetailsByCustomerId(@PathVariable("id") int id) throws OrderNotFoundException{
		
		log.info("In the OrderMangementController we are retriving order by customer Id");
		
		return service.getDetailsByCustomerId(id);
	}
	
	/**
 	 * @param orderReport The `OrderReport` DTO containing the start and end dates.
 	 * @return A ResponseEntity with a list of OrderReportSent objects.
 	 * @throws DateNotFoundException if no orders are found within the date range.
 	 */
	@PostMapping("/getByDate")
	public ResponseEntity<List<OrderReportSent>> getDetailsByDate(@RequestBody OrderReport orderReport)throws DateNotFoundException{
		
		return service.getDetailsByDate(orderReport);
	}
	/**
	 * Endpoint to update the status of a specific order.
	 * @param orderId The ID of the order to be updated.
	 * @param status The new status for the order.
	 * @return A success message.
	 * @throws OrderNotFoundException if the order is not found.
	 */
	@PostMapping("/updateStatus/{orderId}/{status}")
	public String updateStatus(@PathVariable int orderId,@PathVariable String status) throws OrderNotFoundException {
		
		return service.updateStatus(orderId,status);
	}
	
	@GetMapping("/getAll")
	public ResponseEntity<List<Order>> getAllOrders() {
		return service.getAllOrders();
	}
}

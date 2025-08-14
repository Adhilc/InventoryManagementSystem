package com.cts.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cts.client.StockManagementClient;
import com.cts.exception.DataNotFoundException;
import com.cts.exception.DateNotFoundException;
import com.cts.exception.OrderNotFoundException;
import com.cts.model.Order;
import com.cts.model.OrderReport;
import com.cts.model.OrderReportSent;
import com.cts.model.Product;
import com.cts.model.ProductDTO;
import com.cts.model.Stock;
import com.cts.repository.OrderManagementRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderManagementServiceImpl implements OrderManagementService {

	private OrderManagementRepository repo;
	private StockManagementClient sClient;
	public OrderManagementServiceImpl(OrderManagementRepository repo,StockManagementClient sClient) {
		this.repo=repo;
		this.sClient=sClient;
	}
	
	private static final AtomicInteger customerIdCounter=new AtomicInteger(0);
	@Override
	public ResponseEntity<String> createOrder(Product product) throws DataNotFoundException {
		
		log.info("In the OrderManagementService we are creating order");
		 if (product == null || product.getProductId() == 0 || product.getQuantity() <= 0) {
			 	log.error("The data is invalid! please enter the data");
		        throw new DataNotFoundException("Invalid product data. Please provide valid product ID and quantity.");
		    }
		
		Order order=new Order();
		order.setProductId(product.getProductId());
		order.setOrderDate(LocalDateTime.now());
		order.setStatus("Pending");
		order.setQuantity(product.getQuantity());
		order.setCustomerId(customerIdCounter.getAndIncrement());
		
		ProductDTO productDto=new ProductDTO(product.getProductId(),product.getQuantity());
		
		ResponseEntity<Stock> response=sClient.decreaseStockFromOrder(productDto);
		  
		log.info("We are saving the data");
		repo.save(order);
		
		return new ResponseEntity<>("Saved succesfully",HttpStatus.CREATED);
		
	}
	@Override
	public ResponseEntity<Order> getDetailsByOrderId(int id) throws OrderNotFoundException {

		log.info("In the OrderManagementService we are retriving order by order ID");
		Optional<Order>op=repo.findByOrderId(id);
		if (op.isPresent()) {
			return new ResponseEntity<>(op.get(), HttpStatus.OK);
		} else {
			log.error("The Order not found please enter the correct order Id");
			throw new OrderNotFoundException("Order is not found");
		}
	}
	@Override
	public ResponseEntity<List<Order>> getDetailsByCustomerId(int id) throws OrderNotFoundException {
		
		log.info("In the OrderManagementService we are retriveing order by customer ID");
	    List<Order> orders = repo.findByCustomerId(id);
	    
	    if (!orders.isEmpty()) {
	        return new ResponseEntity<>(orders, HttpStatus.OK);
	    } else {
	    	log.error("The Order not found please enter the correct customer Id");
	        throw new OrderNotFoundException("Order is not found");
	    }
	}
	
	public ProductDTO getProductDTO(Product product) {
		log.info("Product data is set into productDto");
		ProductDTO productDto=new ProductDTO();
		productDto.setProductId(product.getProductId());
		productDto.setQuantity(product.getQuantity());
		return null;
		
	}
	
	/**
	 * Retrieves an order report for a specific date range.
	 * It first validates the date range against the earliest and latest order dates
	 * in the database to ensure the request is valid.
	 * @param orderReport The DTO containing the start and end dates.
	 * @return A ResponseEntity with a list of OrderReportSent objects.
	 * @throws DateNotFoundException if no orders are found within the specified date range.
	 */
	@Override
	public ResponseEntity<List<OrderReportSent>> getDetailsByDate(OrderReport orderReport)throws DateNotFoundException {
		
		LocalDateTime startDate=orderReport.getStartDate();
		LocalDateTime endDate=orderReport.getEndDate();
		
		// Add validation to check if the requested date range is within the available data
		// This requires querying for the minimum and maximum order dates in the table.
		Optional<LocalDateTime> minDateOpt = repo.findFirstByOrderByOrderDateAsc().map(Order::getOrderDate);
		Optional<LocalDateTime> maxDateOpt = repo.findFirstByOrderByOrderDateDesc().map(Order::getOrderDate);

		if (minDateOpt.isPresent() && maxDateOpt.isPresent()) {
			LocalDateTime minDate = minDateOpt.get();
			LocalDateTime maxDate = maxDateOpt.get();

			if (startDate.isBefore(minDate) || endDate.isAfter(maxDate)) {
				throw new DateNotFoundException("Requested date range is outside the available data range. Available data is from " + minDate + " to " + maxDate + ".");
			}
		}
		List<OrderReportSent> reportList = repo.findOrderReportByDateBetween(startDate, endDate);
		
		return new ResponseEntity<>(reportList,HttpStatus.OK);
		
	}
	/**
	 * Updates the status of an existing order.
	 * @param orderId The ID of the order to update.
	 * @param status The new status for the order.
	 * @return A success message upon successful update.
	 * @throws OrderNotFoundException if the order with the given ID does not exist.
	 */
	@Override
	public String updateStatus(int orderId, String status) throws OrderNotFoundException {
		// TODO Auto-generated method stub
		
		Optional<Order> optional=repo.findByOrderId(orderId);
		
		if(optional.isEmpty()) {
			throw new OrderNotFoundException("The Order is not found please enter the correct order Id.");
		}
		Order order=optional.get();
		order.setStatus(status);
		repo.save(order);
		return "Successfully Updated status";
	}
	
	
}

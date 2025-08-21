package com.cts.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cts.client.ProductManagementClient;
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

/**
 * Service implementation for managing order-related operations.
 * Implements the OrderManagementService interface.
 */
@Slf4j
@Service
public class OrderManagementServiceImpl implements OrderManagementService {

	// JPA repository for database operations on Order entities.
	private OrderManagementRepository repo;
	// Feign client for communicating with the Stock Management microservice.
	private StockManagementClient sClient;
	// Feign client for communicating with the Product Management microservice.
	private ProductManagementClient pClient;

	/**
	 * Constructor for dependency injection. Spring injects the required repository and Feign clients.
	 * @param repo The repository for order data.
	 * @param sClient The client for the stock management service.
	 * @param pClient The client for the product management service.
	 */
	public OrderManagementServiceImpl(OrderManagementRepository repo, StockManagementClient sClient,
			ProductManagementClient pClient) {
		this.repo = repo;
		this.sClient = sClient;
		this.pClient = pClient;
	}

	// A thread-safe counter to generate unique customer IDs for demonstration purposes.
	private static final AtomicInteger customerIdCounter = new AtomicInteger(0);

	/**
	 * Creates a new order, validates product availability, and decreases stock.
	 * @param product The product information (ID and quantity) for the new order.
	 * @return A ResponseEntity with a success message and HTTP status CREATED.
	 * @throws DataNotFoundException if the product data is invalid, the product is not found, or stock is insufficient.
	 */
	@Override
	public ResponseEntity<String> createOrder(Product product) throws DataNotFoundException {

		log.info("In the OrderManagementService we are creating order");
		// 1. Validate incoming product data.
		if (product == null || product.getProductId() == 0 || product.getQuantity() <= 0) {
			log.info(""+product);
			log.error("The data is invalid! please enter the data");
			throw new DataNotFoundException("Invalid product data. Please provide valid product ID and quantity.");
		}

		// 2. Create and populate a new Order entity.
		Order order = new Order();
		order.setProductId(product.getProductId());
		order.setOrderDate(LocalDate.now()); // Changed from LocalDateTime.of(today, endOfDay)
		order.setQuantity(product.getQuantity());
		order.setCustomerId(customerIdCounter.getAndIncrement());
		order.setStatus("Pending"); // Setting a default status here is better practice

		// 3. Call ProductManagement microservice via Feign client to check product existence and quantity.
		int result = pClient.checkProductId(product.getProductId());
		log.info(result+"");
		if (result == -1) {
			throw new DataNotFoundException("Product not found");
		}else if(result<product.getQuantity()) {
			throw new DataNotFoundException("Quantity is less than ordered.");
		}

		// 4. Prepare a DTO for the stock service call.
		ProductDTO productDto = new ProductDTO(product.getProductId(), product.getQuantity());

		// 5. Call StockManagement microservice via Feign client to decrease the stock.
		ResponseEntity<Stock> response = sClient.decreaseStockFromOrder(productDto);

		// 6. If all remote calls are successful, update order status to Accepted.
		order.setStatus("Accepted");

		// 7. Save the final order details to the database.
		log.info("We are saving the data");
		repo.save(order);

		return new ResponseEntity<>("Saved succesfully", HttpStatus.CREATED);
	}

	/**
	 * Retrieves a single order by its unique order ID.
	 * @param id The ID of the order to retrieve.
	 * @return A ResponseEntity containing the found Order and HTTP status OK.
	 * @throws OrderNotFoundException if no order is found with the given ID.
	 */
	@Override
	public ResponseEntity<Order> getDetailsByOrderId(int id) throws OrderNotFoundException {

		log.info("In the OrderManagementService we are retriving order by order ID");
		Optional<Order> op = repo.findByOrderId(id);
		if (op.isPresent()) {
			return new ResponseEntity<>(op.get(), HttpStatus.OK);
		} else {
			log.error("The Order not found please enter the correct order Id");
			throw new OrderNotFoundException("Order is not found");
		}
	}

	/**
	 * Retrieves all orders placed by a specific customer.
	 * @param id The ID of the customer.
	 * @return A ResponseEntity containing a list of Orders and HTTP status OK.
	 * @throws OrderNotFoundException if the customer has no orders.
	 */
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

	/**
	 * Converts a Product object to a ProductDTO object.
	 * @param product The Product object to convert.
	 * @return A new ProductDTO object. Note: This implementation currently returns null.
	 */
	public ProductDTO getProductDTO(Product product) {
		log.info("Product data is set into productDto");
		ProductDTO productDto = new ProductDTO();
		productDto.setProductID(product.getProductId());
		productDto.setQuantity(product.getQuantity());
		return null;

	}

	/**
	 * Retrieves an order report for a specific date range. It first validates the
	 * date range against the earliest and latest order dates in the database to
	 * ensure the request is valid.
	 * @param orderReport The DTO containing the start and end dates.
	 * @return A ResponseEntity with a list of OrderReportSent objects.
	 * @throws DateNotFoundException if no orders are found within the specified
	 * date range or if the range is invalid.
	 */
	@Override
	public ResponseEntity<List<OrderReportSent>> getDetailsByDate(OrderReport orderReport)throws DateNotFoundException {
		LocalDate startDate=orderReport.getStartDate(); // Changed from LocalDateTime
		LocalDate endDate=orderReport.getEndDate(); // Changed from LocalDateTime
		
		// Validate the requested date range against the available data in the database.
		Optional<Order> minDateOpt = repo.findFirstByOrderByOrderDateAsc();
		Optional<Order> maxDateOpt = repo.findFirstByOrderByOrderDateDesc();

		if (minDateOpt.isPresent() && maxDateOpt.isPresent()) {
			LocalDate minDate = minDateOpt.get().getOrderDate();
			LocalDate maxDate = maxDateOpt.get().getOrderDate();

			// If the requested range is outside the bounds of available data, throw an exception.
			if (startDate.isBefore(minDate) || endDate.isAfter(maxDate)) {
				throw new DateNotFoundException("Requested date range is outside the available data range. Available data is from " + minDate + " to " + maxDate + ".");
			}
		}
		// Fetch the report data from the repository using the validated date range.
		List<OrderReportSent> reportList = repo.findOrderReportByDateBetween(startDate, endDate);
		return new ResponseEntity<>(reportList,HttpStatus.OK);
	}

	/**
	 * Updates the status of an existing order.
	 * @param orderId The ID of the order to update.
	 * @param status  The new status for the order.
	 * @return A success message upon successful update.
	 * @throws OrderNotFoundException if the order with the given ID does not exist.
	 */
	@Override
	public String updateStatus(int orderId, String status) throws OrderNotFoundException {
		// TODO Auto-generated method stub

		// Find the order by its ID.
		Optional<Order> optional = repo.findByOrderId(orderId);

		// If the order doesn't exist, throw an exception.
		if (optional.isEmpty()) {
			throw new OrderNotFoundException("The Order is not found please enter the correct order Id.");
		}
		
		// Get the order, update its status, and save it back to the database.
		Order order = optional.get();
		order.setStatus(status);
		repo.save(order);
		return "Successfully Updated status";
	}

}
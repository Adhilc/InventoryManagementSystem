package com.cts.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cts.model.Order;
import com.cts.model.OrderReportSent;

/**
 * A Spring Data JPA repository for managing {@link Order} entities.
 * Extending JpaRepository provides standard CRUD (Create, Read, Update, Delete)
 * operations out of the box.
 */
public interface OrderManagementRepository extends JpaRepository<Order,Integer> {

	/**
	 * Finds an Order based on the product ID.
	 * Note: This derived query method expects a single result. If multiple orders
	 * could be associated with one product, returning a List or Optional would be safer.
	 * @param productID The ID of the product.
	 * @return The Order associated with the given product ID.
	 */
	Order getByProductId(int productID);

	/**
	 * Finds an Order by its unique primary key (orderId).
	 * @param id The order ID to search for.
	 * @return An {@link Optional} containing the found Order, or an empty Optional if no order is found.
	 */
	Optional<Order> findByOrderId(int id);

	/**
	 * Retrieves all orders associated with a specific customer ID.
	 * @param id The customer ID.
	 * @return A {@link List} of Orders placed by the customer. Returns an empty list if none are found.
	 */
	List<Order> findByCustomerId(int id);

	/**
	 * Executes a custom JPQL query to generate a report of orders within a specific date range.
	 * This method uses a constructor expression to map the results directly into a list of
	 * {@link OrderReportSent} DTOs, which is an efficient way to retrieve only the necessary data.
	 * @param startDate The start date of the reporting period (inclusive).
	 * @param endDate The end date of the reporting period (inclusive).
	 * @return A list of order report data transfer objects.
	 */
	@Query("SELECT new com.cts.model.OrderReportSent(o.productId, o.orderDate, o.quantity) " +
	       "FROM Order o " +
	       "WHERE o.orderDate BETWEEN :startDate AND :endDate")
	List<OrderReportSent> findOrderReportByDateBetween(
	    @Param("startDate") LocalDate startDate, 
	    @Param("endDate") LocalDate endDate 
	);

	/**
	 * Finds the first order in the table, ordered by the order date in ascending order.
	 * Used to determine the minimum date of available data.
	 * @return An Optional containing the oldest Order, or empty if no orders exist.
	 */
	Optional<Order> findFirstByOrderByOrderDateAsc();

	/**
	 * Finds the first order in the table, ordered by the order date in descending order.
	 * Used to determine the maximum date of available data.
	 * @return An Optional containing the most recent Order, or empty if no orders exist.
	 */
	Optional<Order> findFirstByOrderByOrderDateDesc();
}
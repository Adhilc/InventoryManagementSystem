package com.cts.repository;
 
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
 
import com.cts.model.Order;
import com.cts.model.OrderReportSent;
 
 
public interface OrderManagementRepository extends JpaRepository<Order,Integer> {
 
	Order getByProductId(int productID);
 
	Optional<Order> findByOrderId(int id);
 
	List<Order> findByCustomerId(int id);
 
	// After:
	@Query("SELECT new com.cts.model.OrderReportSent(o.productId, o.orderDate, o.quantity) " +
	        "FROM Order o " + // Correct! Use the entity name "Order".
	        "WHERE o.orderDate BETWEEN :startDate AND :endDate")
	List<OrderReportSent> findOrderReportByDateBetween(
	    @Param("startDate") LocalDateTime startDate,
	    @Param("endDate") LocalDateTime endDate
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
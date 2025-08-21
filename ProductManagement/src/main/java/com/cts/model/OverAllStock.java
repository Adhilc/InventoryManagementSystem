package com.cts.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Data Transfer Object (DTO) representing a product's stock information.
 * <p>
 * This class is a simple Plain Old Java Object (POJO) used to encapsulate
 * the product ID, name, and quantity for stock-related operations. It is
 * primarily used to transfer data between different layers of the application
 * (e.g., from the service layer to the controller) and to represent the
 * combined view of product and stock details.
 * </p>
 * <p>
 * The {@code @Data}, {@code @AllArgsConstructor}, and {@code @NoArgsConstructor}
 * annotations are provided by the Lombok library to automatically generate boilerplate code
 * such as getters, setters, constructors, {@code toString()}, {@code equals()}, and
 * {@code hashCode()} methods, reducing verbosity.
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OverAllStock {
	
	/**
	 * The unique identifier for the product.
	 */
	private int productID;
	
	/**
	 * The name of the product.
	 */
	private String name;
	
	/**
	 * The current quantity or stock level of the product.
	 */
	private int quantity;
}

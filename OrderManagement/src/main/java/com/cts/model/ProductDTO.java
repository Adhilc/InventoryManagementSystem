package com.cts.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a Product Data Transfer Object (DTO).
 * <p>
 * DTOs are used to transfer data between different parts of an application,
 * such as between microservices. This class is used to carry a product's ID
 * and a specific quantity for an operation, like updating stock.
 *
 * @Data A Lombok annotation that generates boilerplate code like getters, setters,
 * equals(), hashCode(), and toString().
 * @NoArgsConstructor A Lombok annotation that generates a no-argument constructor.
 * @AllArgsConstructor A Lombok annotation that generates a constructor with all fields as arguments.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

	/**
	 * The unique identifier for the product.
	 */
	private int productID;

	/**
	 * The number of units of the product involved in an operation
	 * (e.g., the quantity to decrease from stock).
	 */
	private int quantity;
}
package com.cts.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A simple DTO (Data Transfer Object) to represent a product with its ID and quantity.
 * This object is used in the request body for the `createOrder` endpoint.
 * * @Data Generates getters, setters, toString(), etc. via Lombok.
 * @AllArgsConstructor Creates a constructor with all fields.
 * @NoArgsConstructor Creates a default, no-argument constructor.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

	/**
	 * The unique identifier for the product.
	 */
	private int productId;

	/**
	 * The number of units of the product requested.
	 */
	private int quantity;
}
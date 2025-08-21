package com.cts.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the stock information for a single product.
 * <p>
 * This class is a simple Plain Old Java Object (POJO) used to model
 * inventory data within the application.
 *
 * @Data A Lombok annotation that generates boilerplate code like getters, setters,
 * equals(), hashCode(), and toString().
 * @NoArgsConstructor A Lombok annotation that generates a no-argument constructor.
 * @AllArgsConstructor A Lombok annotation that generates a constructor with all fields as arguments.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock {

	/**
	 * The unique identifier for the product associated with this stock record.
	 */
	private int productID;

	/**
	 * The current quantity of the product available in stock.
	 */
	private int quantity;

	/**
	 * The reorder level for the product. When the 'quantity' in stock falls
	 * to this level, a new order should be triggered to replenish inventory.
	 */
	private int reorderLevel;
}
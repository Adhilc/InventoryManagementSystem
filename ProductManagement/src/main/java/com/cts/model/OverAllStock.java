package com.cts.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing the overall stock information for a
 * product.
 * <p>
 * This class is used to provide a simplified view of product stock, combining
 * the product ID, name, and current quantity. It's often used for reporting or
 * displaying a list of products with their stock levels. The Lombok annotations
 * automatically generate the boilerplate code for constructors, getters, and
 * setters.
 * </p>
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OverAllStock {

	/**
	 * The unique identifier of the product.
	 */
	private int productID;

	/**
	 * The name of the product.
	 */
	private String name;

	/**
	 * The current quantity of the product in stock.
	 */
	private int quantity;

}
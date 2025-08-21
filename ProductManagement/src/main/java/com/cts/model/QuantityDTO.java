package com.cts.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for updating the quantity of a product.
 * <p>
 * This class is used to encapsulate the product ID and the new quantity
 * when an update request is made to the system. It uses Lombok annotations
 * to automatically generate boilerplate code for getters, setters,
 * constructors, and other common methods.
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuantityDTO {
	/**
	 * The unique identifier of the product whose quantity needs to be updated.
	 */
	private int productID;
	
	/**
	 * The new quantity to be set for the product.
	 */
	private int quantity;
}
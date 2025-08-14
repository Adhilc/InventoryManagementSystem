package com.cts.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for product information.
 * <p>
 * This class is used to transfer a simplified view of a product,
 * typically including essential details like ID, name, and quantity,
 * without exposing the full {@link Product} entity. It uses Lombok
 * annotations to automatically generate boilerplate code.
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
	
	/**
	 * The unique identifier of the product.
	 */
	private int productID;
	
	/**
	 * The name of the product.
	 */
	private String name;
	
	/**
	 * The current stock quantity of the product.
	 */
	private int quantity;

}
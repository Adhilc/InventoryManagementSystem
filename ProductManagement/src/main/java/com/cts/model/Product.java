package com.cts.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a Product entity in the application.
 * <p>
 * This class is a JPA {@code @Entity} that maps to the "product" table in the
 * database. It includes validation constraints to ensure data integrity. LomboK
 * annotations are used to reduce boilerplate code for constructors, getters,
 * and setters.
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
@Entity
public class Product {

	/**
	 * The unique identifier for the product. It serves as the primary key. It must
	 * not be null and must be a positive integer.
	 */
	@Id
	@NotNull(message = "ID Cannot be given as Null")
	@Min(value = 1, message = "ID Cannot be a Value less than 0")
	private int productID;

	/**
	 * The name of the product. It cannot be null or empty.
	 */
	@NotEmpty(message = "Product Name Cannot be given as Empty")
	private String name;

	/**
	 * A brief description of the product. It cannot be null or empty.
	 */
	@NotEmpty(message = "Product Description Cannot be given as Empty")
	private String description;

	/**
	 * The price of the product. It must be at least 10.
	 */
	@Min(value = 10, message = "Product Price Cannot be Less than 10")
	private int price;

	/**
	 * The current quantity of the product available in stock.
	 */
	private int stockLevel;
}
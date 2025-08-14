package com.cts.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

/**
* A simple DTO to represent a product with its ID and quantity.
* This object is used in the request body for the `createOrder` endpoint.
*/
public class Product {

	private int productId;
	private int quantity;
}

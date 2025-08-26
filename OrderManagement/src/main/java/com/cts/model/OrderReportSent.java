package com.cts.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
* DTO used to represent the data for an order report.
* It contains a subset of `Order` fields, specifically for the report view.
* The JPQL query in the repository directly populates this object, which is more efficient
* than fetching the full `Order` entity.
*/
public class OrderReportSent {

	/**
	 * The unique identifier for the product included in the order.
	 */
	private int productId;

	/**
	 * The date when the order was placed.
	 */
	private LocalDate date;

	/**
	 * The number of units of the product ordered.
	 */
	private int quantity;
}
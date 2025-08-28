package com.cts.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) to encapsulate the start and end dates for an order report query.
 * It's used to pass the date range from the controller to the service layer.
 * * @Data is a Lombok annotation for generating getters, setters, etc.
 * @AllArgsConstructor creates a constructor with all fields.
 * @NoArgsConstructor creates a default, no-argument constructor.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderReport {

	/**
	 * The start date for the report period.
	 */
	private LocalDate startDate; 

	/**
	 * The end date for the report period.
	 */
	private LocalDate endDate;
}
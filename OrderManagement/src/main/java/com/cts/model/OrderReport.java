package com.cts.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* DTO (Data Transfer Object) to encapsulate the start and end dates for an order report query.
* It's used to pass the date range from the controller to the service layer.
*/

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderReport {

	private LocalDateTime startDate;
	private LocalDateTime endDate;
}

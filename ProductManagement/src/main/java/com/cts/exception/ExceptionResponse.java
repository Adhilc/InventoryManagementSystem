package com.cts.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A standard structure for API error responses. 📝
 * <p>
 * This is a Plain Old Java Object (POJO) used as a Data Transfer Object (DTO)
 * to ensure that all error messages sent to the client have a consistent
 * format. The Lombok annotations significantly reduce boilerplate code:
 * <ul>
 * <li>{@code @Data}: Generates getters, setters, {@code toString()},
 * {@code equals()}, and {@code hashCode()} methods.</li>
 * <li>{@code @NoArgsConstructor}: Creates a constructor with no arguments.</li>
 * <li>{@code @AllArgsConstructor}: Creates a constructor with all fields as
 * arguments.</li>
 * </ul>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse {

	/**
	 * The HTTP status code or a custom application-specific status code.
	 */
	private int status;

	/**
	 * A user-friendly message describing the error.
	 */
	private String message;

	/**
	 * The timestamp indicating when the error occurred.
	 */
	private LocalDateTime time;
}
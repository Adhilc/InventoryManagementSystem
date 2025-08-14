package com.cts.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
* Centralized exception handler for the application.
* `@RestControllerAdvice` allows it to handle exceptions thrown by any `@RestController`.
*/
@RestControllerAdvice
public class GlobalExceptionHandler {
	
	/**
 	 * Handles `OrderNotFoundException` by returning an HTTP 404 Not Found status.
 	 * @param ex The exception that was thrown.
 	 * @return A `ResponseEntity` containing an `ErrorResponse` DTO and a 404 status.
 	 */

	@ExceptionHandler(value=OrderNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleOrderNotFound(OrderNotFoundException ex){
			
		ErrorResponse errResponse=new ErrorResponse(HttpStatus.NOT_FOUND.value(),ex.getMessage(),LocalDateTime.now());
		
		return new ResponseEntity<>(errResponse,HttpStatus.NOT_FOUND);
	}
	
	/**
 	 * Handles `DataNotFoundException` by returning an HTTP 400 Bad Request status.
 	 * This is used for invalid input data.
 	 * @param ex The exception that was thrown.
 	 * @return A `ResponseEntity` containing an `ErrorResponse` DTO and a 400 status.
 	 */
	@ExceptionHandler(value=DataNotFoundException.class)
	public ResponseEntity<ErrorResponse>handleDataNotFound(DataNotFoundException ex){
		
		ErrorResponse errResponse=new ErrorResponse(HttpStatus.BAD_REQUEST.value(),ex.getMessage(),LocalDateTime.now());
		
		return new ResponseEntity<>(errResponse,HttpStatus.BAD_REQUEST);
	}
	
	/**
 	 * Handles the new `DateNotFoundException` by returning an HTTP 404 Not Found status.
 	 * This exception is specific to cases where no records are found for a date-based query.
 	 * @param ex The exception that was thrown.
 	 * @return A `ResponseEntity` containing an `ErrorResponse` DTO and a 404 status.
 	 */
	
	@ExceptionHandler(value=DateNotFoundException.class)
 	public ResponseEntity<ErrorResponse>handleDateNotFound(DateNotFoundException ex){
		
 		ErrorResponse errResponse=new ErrorResponse(HttpStatus.NOT_FOUND.value(),ex.getMessage(),LocalDateTime.now());
		
 		return new ResponseEntity<>(errResponse,HttpStatus.NOT_FOUND);
 	}
}

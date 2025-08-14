package com.cts.exception;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * Global Exception Handler for the application.
 * <p>
 * This class uses {@code @ControllerAdvice} to intercept exceptions thrown from
 * any controller across the application. It centralizes exception handling
 * logic and ensures that consistent, well-formed error responses are sent back
 * to the client.
 * </p>
 */
@ControllerAdvice
public class CustomGlobalExceptionHandler {

	/**
	 * Handles validation errors triggered by the {@code @Valid} annotation.
	 * <p>
	 * This method catches {@link MethodArgumentNotValidException} and extracts all
	 * field-specific validation errors. It creates a JSON response mapping each
	 * invalid field to its corresponding error message.
	 * </p>
	 *
	 * @param ex The captured {@code MethodArgumentNotValidException}.
	 * @return A {@link ResponseEntity} with a map of validation errors and an HTTP
	 *         status of 400 (Bad Request).
	 */
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", new Date());

		// Get all validation errors and add them to the response body
		ex.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			body.put(fieldName, errorMessage);
		});

		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handles the custom {@link ProductNotFound} exception.
	 * <p>
	 * This method is triggered when a service or repository fails to find a
	 * product. It constructs a standardized {@link ExceptionResponse} object. Note:
	 * The HTTP status returned is NOT_ACCEPTABLE (406), which may be unconventional
	 * for a "not found" scenario.
	 * </p>
	 *
	 * @param exception  The caught {@code ProductNotFound} exception.
	 * @param webRequest The current web request details.
	 * @return A {@link ResponseEntity} containing the custom error response and
	 *         HTTP status 406 (Not Acceptable).
	 */
	@ExceptionHandler(value = ProductNotFound.class)
	public ResponseEntity<ExceptionResponse> handleProductNotFoundException(ProductNotFound exception,
			WebRequest webRequest) {
		ExceptionResponse exceptionResponse = new ExceptionResponse();
		exceptionResponse.setStatus(404); // Sets a custom status in the body
		exceptionResponse.setTime(LocalDateTime.now());
		exceptionResponse.setMessage(exception.getMessage());

		return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_ACCEPTABLE);
	}

	/**
	 * A generic, catch-all exception handler.
	 * <p>
	 * This method handles any exception that is not caught by the more specific
	 * handlers above. It acts as a fallback to prevent unhandled server errors from
	 * reaching the client. Note: The HTTP status returned is NOT_ACCEPTABLE (406),
	 * which is unusual for a generic server error. A 500 (Internal Server Error)
	 * status is more common.
	 * </p>
	 *
	 * @param exception  The caught {@code Exception}.
	 * @param webRequest The current web request details.
	 * @return A {@link ResponseEntity} containing the custom error response and
	 *         HTTP status 406 (Not Acceptable).
	 */
	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<ExceptionResponse> handleGenericException(Exception exception, WebRequest webRequest) {
		ExceptionResponse exceptionResponse = new ExceptionResponse();
		exceptionResponse.setStatus(404); // Sets a custom status in the body
		exceptionResponse.setTime(LocalDateTime.now());
		exceptionResponse.setMessage(exception.getMessage());

		return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_ACCEPTABLE);
	}
}
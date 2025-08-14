package com.cts.stockmanagementexceptions;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;

/**
 * A Global Exception Handler for a REACTIVE (WebFlux) application.
 * This class uses @RestControllerAdvice and handles exceptions for REST controllers.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles InsufficientStockException and returns a 400 Bad Request.
     */
    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<Object> handleInsufficientStockException(InsufficientStockException ex, ServerWebExchange exchange) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, exchange);
    }

    /**
     * Handles StockNotFoundException and returns a 404 Not Found.
     */
    @ExceptionHandler(StockNotFoundException.class)
    public ResponseEntity<Object> handleStockNotFoundException(StockNotFoundException ex, ServerWebExchange exchange) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, exchange);
    }

    /**
     * Handles InvalidStockAmountException and returns a 400 Bad Request.
     */
    @ExceptionHandler(InvalidStockAmountException.class)
    public ResponseEntity<Object> handleInvalidStockAmountException(InvalidStockAmountException ex, ServerWebExchange exchange) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, exchange);
    }

    /**
     * Handles the generic NoSuchElementException. This is the fix for your current error.
     * It correctly uses ServerWebExchange instead of WebRequest.
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException ex, ServerWebExchange exchange) {
        String message = "The requested resource was not found.";
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, exchange, message);
    }

    /**
     * A helper method to build the standardized error response body using ServerWebExchange.
     */
    private ResponseEntity<Object> buildErrorResponse(Exception ex, HttpStatus status, ServerWebExchange exchange) {
        return buildErrorResponse(ex, status, exchange, ex.getMessage());
    }

    /**
     * An overloaded helper method to allow for a custom message.
     */
    private ResponseEntity<Object> buildErrorResponse(Exception ex, HttpStatus status, ServerWebExchange exchange, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        // Get the path from the reactive ServerWebExchange
        body.put("path", exchange.getRequest().getPath().value());

        return new ResponseEntity<>(body, status);
    }
}

package com.cts.stockmanagementexceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when an invalid amount (e.g., zero or negative) is used for stock operations.
 * Responds with HTTP 400 Bad Request.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidStockAmountException extends RuntimeException {
    public InvalidStockAmountException(String message) {
        super(message);
    }
}
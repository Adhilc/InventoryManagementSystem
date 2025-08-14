package com.cts.stockmanagementexceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception to be thrown when a stock decrease operation
 * cannot be completed due to insufficient quantity.
 * The @ResponseStatus annotation tells Spring to respond with a 400 BAD REQUEST
 * status code whenever this exception is thrown and not caught elsewhere.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(String message) {
        super(message);
    }
}
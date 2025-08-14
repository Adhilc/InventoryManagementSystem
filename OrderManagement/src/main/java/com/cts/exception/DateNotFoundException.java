package com.cts.exception;


/**
* Custom exception to be thrown when a query for a date range returns no results.
* It extends `Exception` to be a checked exception, forcing developers to handle it explicitly.
*/
public class DateNotFoundException extends Exception {

	public DateNotFoundException(String message) {
		super(message);
	}
}

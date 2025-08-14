package com.cts.exception;

/**
 * A custom checked exception that is thrown when a requested product cannot be
 * found.
 * <p>
 * This exception is typically thrown by service or repository layers when an
 * operation (like find, update, or delete) is performed with an ID that does
 * not correspond to any existing product in the database.
 * </p>
 */
public class ProductNotFound extends Exception {

	/**
	 * The unique version identifier for this serializable class. This is used
	 * during the deserialization process to verify that the sender and receiver of
	 * a serialized object have loaded classes for that object that are compatible
	 * with respect to serialization.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new {@code ProductNotFound} exception with the specified detail
	 * message.
	 *
	 * @param message The detail message, which is saved for later retrieval by the
	 *                {@link #getMessage()} method.
	 */
	public ProductNotFound(String message) {
		super(message);
	}
}
package com.cts.service;

import java.util.List;

import org.springframework.web.bind.MethodArgumentNotValidException;

import com.cts.exception.ProductNotFound;
import com.cts.model.OverAllStock;
import com.cts.model.Product;
import com.cts.model.ProductDTO;
import com.cts.model.QuantityDTO;

/**
 * Defines the contract for the business logic layer for product-related
 * operations. ⚙️
 * <p>
 * This interface outlines all the business operations that can be performed on
 * {@link Product} entities. The implementation of this interface will contain
 * the core business logic, separating it from the web (controller) and data
 * access (repository) layers.
 * </p>
 */
public interface ProductService {

	/**
	 * Saves a new product to the system.
	 *
	 * @param product The {@link Product} object to be created and saved.
	 * @return A confirmation message indicating the result of the operation.
	 * @throws MethodArgumentNotValidException if the provided product object is
	 *                                         invalid.
	 */
	public Product saveProduct(Product product) throws MethodArgumentNotValidException;

	/**
	 * Updates the details of an existing product.
	 *
	 * @param product The {@link Product} object containing the updated information.
	 * @return A confirmation message indicating the result of the operation.
	 * @throws MethodArgumentNotValidException if the provided product object is
	 *                                         invalid.
	 * @throws ProductNotFound                 if the product to be updated is not
	 *                                         found.
	 */
	public String updateProduct(Product product) throws MethodArgumentNotValidException, ProductNotFound;

	/**
	 * Deletes a product from the system based on its unique ID.
	 *
	 * @param id The ID of the product to be deleted.
	 * @return A confirmation message indicating the result of the operation.
	 * @throws ProductNotFound if no product with the specified ID can be found.
	 */
	public String deleteProductById(int id) throws ProductNotFound;

	/**
	 * Retrieves a list of all products that are currently available (in stock).
	 *
	 * @return A list of available {@link Product}s.
	 */
	public List<Product> getAllAvailableProducts();

	/**
	 * Retrieves a complete list of all products in the system, regardless of stock
	 * level.
	 *
	 * @return A list of all {@link Product}s.
	 */
	public List<Product> getAllProducts();

	/**
	 * Retrieves a list of products whose price falls within a given range.
	 *
	 * @param initial The starting price of the range (inclusive).
	 * @param fina    The final price of the range (inclusive).
	 * @return A list of {@link Product}s that match the price criteria.
	 */
	public List<Product> getProductsBetweenPriceRange(int initial, int fina);

	/**
	 * Retrieves the name of a product by its ID.
	 *
	 * @param id The ID of the product.
	 * @return The name of the product as a String.
	 * @throws ProductNotFound if no product with the specified ID is found.
	 */
	public String getProductName(int id) throws ProductNotFound;

	/**
	 * Retrieves a list of all products along with their stock details.
	 *
	 * @return A list of {@link OverAllStock} objects, each containing product ID,
	 *         name, and quantity.
	 */
	public List<OverAllStock> getAllStocks();

	/**
	 * Retrieves a list of products with their names and quantities.
	 *
	 * @return A list of {@link ProductDTO} objects.
	 */
	public List<ProductDTO> getAllProductQuantity();

	/**
	 * Updates the quantity of a specific product.
	 *
	 * @param quantityDTO A {@link QuantityDTO} containing the product ID and the
	 *                    new quantity.
	 * @return A confirmation message indicating the result of the operation.
	 */
	public String updateQuantity(QuantityDTO quantityDTO);

	/**
	 * Checks if a product with the given ID exists in the system.
	 *
	 * @param id The ID of the product to check.
	 * @return The stock level of the product if it exists; otherwise, -1.
	 */
	public int checkProductId(int id);

}
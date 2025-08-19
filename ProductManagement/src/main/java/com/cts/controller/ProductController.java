package com.cts.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.exception.ProductNotFound;
import com.cts.model.OverAllStock;
import com.cts.model.Product;
import com.cts.model.ProductDTO;
import com.cts.model.QuantityDTO;
import com.cts.service.ProductService;

import jakarta.validation.Valid;

/**
 * REST Controller for managing products.
 * <p>
 * This class handles all HTTP requests related to products, such as creating,
 * updating, deleting, and retrieving product information. All endpoints are
 * prefixed with "/product".
 * </p>
 */
@RestController
@RequestMapping("/api/product")
public class ProductController {

	/**
	 * Autowired instance of ProductService to handle business logic. Spring's
	 * dependency injection automatically provides an instance of ProductService.
	 */
	@Autowired
	ProductService service;

	/**
	 * Handles POST requests to add a new product to the system. The {@code @Valid}
	 * annotation triggers validation on the product object.
	 *
	 * @param product The product data from the request body. Must be a valid
	 *                Product object.
	 * @return A confirmation message from the service layer.
	 * @throws MethodArgumentNotValidException if the product object fails
	 *                                         validation.
	 */
	@PostMapping("/add")
	public String saveProduct(@Valid @RequestBody Product product) throws MethodArgumentNotValidException {
		return service.saveProduct(product);
	}

	/**
	 * Handles GET requests to retrieve a list of all products with their stock
	 * information.
	 *
	 * @return A list of {@link OverAllStock} objects containing product and stock
	 *         details.
	 */
	@GetMapping("/getAll")
	public List<OverAllStock> getAllProductsStocks() {
		return service.getAllStocks();
	}

	/**
	 * Handles PUT requests to update an existing product. The {@code @Valid}
	 * annotation triggers validation on the product object.
	 *
	 * @param product The updated product data from the request body. Must be a
	 *                valid Product object.
	 * @return A confirmation message from the service layer.
	 * @throws MethodArgumentNotValidException if the product object fails
	 *                                         validation.
	 */
	@PutMapping("/update")
	public String updateProduct(@Valid @RequestBody Product product)
			throws MethodArgumentNotValidException, ProductNotFound {
		return service.updateProduct(product);
	}

	/**
	 * Handles GET requests to retrieve the name of a product by its ID.
	 *
	 * @param id The ID of the product, extracted from the URL path.
	 * @return The name of the product.
	 * @throws ProductNotFound if no product with the given ID is found.
	 */
	@GetMapping("/getProductName/{id}")
	public String getProductName(@PathVariable("id") int id) throws ProductNotFound {
		return service.getProductName(id);
	}

	/**
	 * Handles DELETE requests to remove a product by its ID.
	 *
	 * @param id The ID of the product to delete, extracted from the URL path.
	 * @return A confirmation message from the service layer.
	 * @throws ProductNotFound if no product with the given ID is found.
	 */
	@DeleteMapping("/deleteById/{id}")
	public String deleteProduct(@PathVariable("id") int id) throws ProductNotFound {
		return service.deleteProductById(id);
	}

	/**
	 * Handles GET requests to retrieve a list of all available products.
	 *
	 * @return A list of {@link Product} objects that are currently available.
	 */
	@GetMapping("/viewAllAvailable")
	public List<Product> getAllAvailableProducts() {
		return service.getAllAvailableProducts();
	}

	/**
	 * Handles GET requests to retrieve a list of all products, regardless of
	 * availability.
	 *
	 * @return A complete list of all {@link Product} objects in the system.
	 */
	@GetMapping("/viewAll")
	public List<Product> getAllProducts() {
		return service.getAllProducts();
	}

	/**
	 * Handles GET requests to find products within a specified price range.
	 *
	 * @param initial The starting price of the range, from the URL path.
	 * @param fina    The final (ending) price of the range, from the URL path.
	 * @return A list of {@link Product} objects that fall within the specified
	 *         price range.
	 */
	@GetMapping("/viewBasedOnPriceRange/{initial}/{fina}")
	public List<Product> getProductsBetweenPriceRange(@PathVariable("initial") int initial,
			@PathVariable("fina") int fina) {
		return service.getProductsBetweenPriceRange(initial, fina);
	}

	/**
	 * Handles GET requests to retrieve the product name and quantity for all
	 * products.
	 * 
	 * @return A list of {@link ProductDTO} objects, each containing a product's
	 *         name and quantity.
	 */
	@GetMapping("/getProductQuantiy")
	public List<ProductDTO> getAllProductQuantity() {
		return service.getAllProductQuantity();
	}

	/**
	 * Handles PUT requests to update the quantity of a product.
	 *
	 * @param quantityDTO An object containing the product ID and the new quantity
	 *                    to be set.
	 * @return A confirmation message from the service layer.
	 */
	@PutMapping("/updateQuantity")
	public String updateQuantity(@RequestBody QuantityDTO quantityDTO) {
		return service.updateQuantity(quantityDTO);
	}
	
	

}
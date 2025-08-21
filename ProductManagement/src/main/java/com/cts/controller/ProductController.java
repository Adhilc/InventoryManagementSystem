package com.cts.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cts.client.StockManagementClient;
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

	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

	/**
	 * Autowired instance of ProductService to handle business logic. Spring's
	 * dependency injection automatically provides an instance of ProductService.
	 */
	@Autowired
	ProductService service;
	

	@Autowired
	StockManagementClient stockManagementClient;

	/**
	 * Handles POST requests to add a new product to the system. The {@code @Valid}
	 * annotation triggers validation on the product object.
	 *
	 * @param product The product data from the request body. Must be a valid
	 * Product object.
	 * @return A confirmation message from the service layer.
	 * @throws MethodArgumentNotValidException if the product object fails
	 * validation.
	 */
	@PostMapping("/add")
	public Product saveProduct(@Valid @RequestBody Product product) throws MethodArgumentNotValidException {
		logger.info("Received request to save a new product: {}", product.getName());
		Product savedProduct = service.saveProduct(product);
		logger.info("Successfully saved product with ID: {}", savedProduct.getProductID());
		return savedProduct;
	}

	/**
	 * Handles GET requests to retrieve a list of all products with their stock
	 * information.
	 *
	 * @return A list of {@link OverAllStock} objects containing product and stock
	 * details.
	 */
	@PostMapping("/getAll")
	public List<OverAllStock> getAllProductsStocks() {
		logger.info("Received request to retrieve all products with stock information.");
		List<OverAllStock> stocks = service.getAllStocks();
		logger.info("Successfully retrieved {} products with stock information.", stocks.size());
		return stocks;
	}

	/**
	 * Handles PUT requests to update an existing product. The {@code @Valid}
	 * annotation triggers validation on the product object.
	 *
	 * @param product The updated product data from the request body. Must be a
	 * valid Product object.
	 * @return A confirmation message from the service layer.
	 * @throws MethodArgumentNotValidException if the product object fails
	 * validation.
	 */
	@PutMapping("/update")
	public String updateProduct(@Valid @RequestBody Product product)
			throws MethodArgumentNotValidException, ProductNotFound {
		logger.info("Received request to update product with ID: {}", product.getProductID());
		String result = service.updateProduct(product);
		logger.info("Product with ID {} updated successfully. Message: {}", product.getProductID(), result);
		return result;
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
		logger.info("Received request to get product name for ID: {}", id);
		String productName = service.getProductName(id);
		logger.info("Found product name for ID {}: {}", id, productName);
		return productName;
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
		logger.info("Received request to delete product with ID: {}", id);
		String result = service.deleteProductById(id);
		logger.info("Product with ID {} deleted successfully. Message: {}", id, result);
		return result;
	}

	/**
	 * Handles GET requests to retrieve a list of all available products.
	 *
	 * @return A list of {@link Product} objects that are currently available.
	 */
	@GetMapping("/viewAllAvailable")
	public List<Product> getAllAvailableProducts() {
		logger.info("Received request to view all available products.");
		List<Product> products = service.getAllAvailableProducts();
		logger.info("Successfully retrieved {} available products.", products.size());
		return products;
	}

	/**
	 * Handles GET requests to retrieve a list of all products, regardless of
	 * availability.
	 *
	 * @return A complete list of all {@link Product} objects in the system.
	 */
	@GetMapping("/viewAll")
	public List<Product> getAllProducts() {
		logger.info("Received request to view all products.");
		List<Product> products = service.getAllProducts();
		logger.info("Successfully retrieved a total of {} products.", products.size());
		return products;
	}

	/**
	 * Handles GET requests to find products within a specified price range.
	 *
	 * @param initial The starting price of the range, from the URL path.
	 * @param fina    The final (ending) price of the range, from the URL path.
	 * @return A list of {@link Product} objects that fall within the specified
	 * price range.
	 */
	@GetMapping("/viewBasedOnPriceRange/{initial}/{fina}")
	public List<Product> getProductsBetweenPriceRange(@PathVariable("initial") int initial,
			@PathVariable("fina") int fina) {
		logger.info("Received request to view products between price range {} and {}", initial, fina);
		List<Product> products = service.getProductsBetweenPriceRange(initial, fina);
		logger.info("Successfully retrieved {} products within the specified price range.", products.size());
		return products;
	}

	/**
	 * Handles GET requests to retrieve the product name and quantity for all
<<<<<<< HEAD
	 * products.
	 * * @return A list of {@link ProductDTO} objects, each containing a product's
	 * name and quantity.
=======
	 * products. * @return A list of {@link ProductDTO} objects, each containing a
	 * product's name and quantity.
>>>>>>> 8be4027cfa2df15e4a8df7b0fac05f90710844cd
	 */
	@GetMapping("/getProductQuantiy")
	public List<ProductDTO> getAllProductQuantity() {
		logger.info("Received request to get all product quantities.");
		List<ProductDTO> quantities = service.getAllProductQuantity();
		logger.info("Successfully retrieved quantities for {} products.", quantities.size());
		return quantities;
	}

	/**
	 * Handles PUT requests to update the quantity of a product.
	 *
	 * @param quantityDTO An object containing the product ID and the new quantity
	 * to be set.
	 * @return A confirmation message from the service layer.
	 */
	@PutMapping("/updateQuantity")
	public String updateQuantity(@RequestBody QuantityDTO quantityDTO) {
		logger.info("Received request to update quantity for product ID: {} to new quantity: {}",
				quantityDTO.getProductID(), quantityDTO.getQuantity());
		String result = service.updateQuantity(quantityDTO);
		logger.info("Quantity updated for product ID {}. Message: {}", quantityDTO.getProductID(), result);
		return result;
	}
	
	/**
	 * Handles POST requests to check if a product ID exists.
	 *
	 * @param id The ID of the product to check.
	 * @return The product ID if it exists.
	 */
	@PostMapping("/checkProductId")
	public int checkProductId(@RequestParam int id) {
		logger.info("Received request to check if product ID {} exists.", id);
		int foundId = service.checkProductId(id);
		logger.info("Product ID check for {} returned: {}", id, foundId);
		return foundId;
	}
}
//
//	/**
//	 * Handles POST requests to check if a product ID exists.
//	 *
//	 * @param id The ID of the product to check.
//	 * @return The product ID if it exists.
//	 */
//	@PostMapping("/checkProductId")
//	public int checkProductId(@RequestParam int id) {
//		logger.info("Received request to check if product ID {} exists.", id);
//		int foundId = service.checkProductId(id);
//		logger.info("Product ID check for {} returned: {}", id, foundId);
//		return foundId;
//	}

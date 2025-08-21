package com.cts.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cts.client.StockManagementClient;
import com.cts.exception.ProductNotFound;
import com.cts.model.OverAllStock;
import com.cts.model.Product;
import com.cts.model.ProductDTO;
import com.cts.model.QuantityDTO;
import com.cts.model.StockDTO;
import com.cts.repository.ProductRepository;

/**
 * The concrete implementation of the {@link ProductService} interface. ⚙️
 * <p>
 * This class contains the core business logic for managing products. It is
 * marked as a {@code @Service}, making it a Spring-managed bean that can be
 * injected into other components like controllers. It interacts with the
 * database through the injected {@link ProductRepository}.
 * </p>
 */
@Service
public class ProductServiceImpl implements ProductService {

	private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

	/**
	 * The repository for performing database operations on Product entities.
	 * Spring's dependency injection provides this instance at runtime.
	 */
	@Autowired
	ProductRepository repo;

	@Autowired
	StockManagementClient sClient;

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation saves the product by calling the {@code save} method of
	 * the {@link ProductRepository}. It also creates a corresponding stock entry
	 * in the stock management microservice.
	 * </p>
	 */
	@Override
	public Product saveProduct(Product product) throws MethodArgumentNotValidException {
		logger.info("Attempting to save new product: {}", product.getName());
		StockDTO stockDto = new StockDTO();
		stockDto.setName(product.getName());
		stockDto.setProductID(product.getProductID());
		stockDto.setQuantity(product.getStockLevel());

		// Call the stock management microservice to create the stock entry
		logger.info("Calling stock management microservice to save stock for product ID: {}", product.getProductID());
		sClient.saveStock(stockDto);
		sClient.save(stockDto);
		logger.info("Stock entry successfully created for product ID: {}", product.getProductID());

		Product savedProduct = repo.save(product);
		logger.info("Product saved successfully with ID: {}", savedProduct.getProductID());
		return savedProduct;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation first checks if a product with the given ID exists. If it
	 * exists, the product's data is updated. If not, it throws a
	 * {@link ProductNotFound} exception to prevent creating a new product via the
	 * update endpoint.
	 * </p>
	 * @throws ProductNotFound if no product with the specified ID is found.
	 */
	@Override
	public String updateProduct(Product product) throws MethodArgumentNotValidException, ProductNotFound {
		int productId = product.getProductID();
		logger.info("Attempting to update product with ID: {}", productId);

		if (repo.existsById(productId)) {
			repo.save(product);
			logger.info("Product with ID {} updated successfully.", productId);
			return "Product Updated Successfully";
		} else {
			logger.error("Update failed: Product with ID {} not found.", productId);
			throw new ProductNotFound("Update failed: Product with ID " + productId + " not found.");
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation first uses {@code findById} to check if a product with
	 * the given ID exists. If the returned {@link Optional} is empty, it throws a
	 * {@link ProductNotFound} exception. Otherwise, it proceeds to delete the
	 * product from the database using {@code deleteById}.
	 * </p>
	 */
	@Override
	public String deleteProductById(int id) throws ProductNotFound {
		logger.info("Attempting to delete product with ID: {}", id);
		Optional<Product> optional = repo.findById(id);
		if (optional.isEmpty()) {
			logger.error("Delete failed: Product with ID {} not found.", id);
			throw new ProductNotFound("Product with ID " + id + " not found.");
		} else {
			repo.deleteById(id);
			logger.info("Product with ID {} deleted successfully.", id);
			return "Product deleted Successfully";
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation defines "available" as having a stock level greater than
	 * zero. It calls the custom repository method
	 * {@code findByStockLevelGreaterThan(0)} to fetch only the products that are in
	 * stock.
	 * </p>
	 */
	@Override
	public List<Product> getAllAvailableProducts() {
		logger.info("Fetching all available products (stock level > 0).");
		List<Product> availableProducts = repo.findByStockLevelGreaterThan(0);
		logger.info("Found {} available products.", availableProducts.size());
		return availableProducts;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation calls the repository's standard {@code findAll()} method
	 * to retrieve all product records from the database without any filtering.
	 * </p>
	 */
	@Override
	public List<Product> getAllProducts() {
		logger.info("Fetching all products.");
		List<Product> allProducts = repo.findAll();
		logger.info("Found a total of {} products.", allProducts.size());
		return allProducts;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation delegates the search directly to the repository's
	 * custom-derived query method {@code findByPriceBetween(initial, fina)}.
	 * </p>
	 */
	@Override
	public List<Product> getProductsBetweenPriceRange(int initial, int fina) {
		logger.info("Fetching products with price between {} and {}.", initial, fina);
		List<Product> products = repo.findByPriceBetween(initial, fina);
		logger.info("Found {} products within the specified price range.", products.size());
		return products;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation checks for the existence of the product by ID and, if
	 * found, retrieves its name. It throws a {@link ProductNotFound} if the ID does
	 * not exist.
	 * </p>
	 */
	@Override
	public String getProductName(int id) throws ProductNotFound {
		logger.info("Attempting to get product name for ID: {}", id);
		boolean result = repo.existsById(id);
		if (result) {
			Product pr = repo.getReferenceById(id);
			logger.info("Found product name for ID {}: {}", id, pr.getName());
			return pr.getName();
		} else {
			logger.error("Product with ID {} not found.", id);
			throw new ProductNotFound("Product Not Found");
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation retrieves a list of all products with their corresponding
	 * stock information by calling a custom query method in the repository.
	 * </p>
	 */
	@Override
	public List<OverAllStock> getAllStocks() {
		logger.info("Fetching all products with their stock information.");
		List<OverAllStock> stocks = repo.getAllStocks();
		logger.info("Successfully retrieved stock information for {} products.", stocks.size());
		return stocks;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation retrieves a simplified list of product details (ID, name,
	 * quantity) by calling a custom query method in the repository.
	 * </p>
	 */
	@Override
	public List<ProductDTO> getAllProductQuantity() {
		logger.info("Fetching all product quantities.");
		List<ProductDTO> productQuantities = repo.getAllProductQuantity();
		logger.info("Successfully retrieved quantities for {} products.", productQuantities.size());
		return productQuantities;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation updates the quantity of a product. It first finds the
	 * product by its ID, updates the stock level using the data from the
	 * {@link QuantityDTO}, and then saves the updated product back to the database.
	 * </p>
	 */
	@Override
	public String updateQuantity(QuantityDTO quantityDTO) {
		logger.info("Attempting to update quantity for product with ID: {} to {}", 
				quantityDTO.getProductID(), quantityDTO.getQuantity());
		Product product = repo.findByProductID(quantityDTO.getProductID());
		if (product != null) {
			product.setStockLevel(quantityDTO.getQuantity());
			repo.save(product);
			logger.info("Quantity successfully updated for product ID: {}", quantityDTO.getProductID());
			return "Successfully updated quantity";
		} else {
			logger.error("Failed to update quantity: Product with ID {} not found.", quantityDTO.getProductID());
			return "Product not found";
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation checks if a product with the given ID exists. If it does,
	 * it returns the current stock level. Otherwise, it returns -1 to indicate that
	 * the product was not found.
	 * </p>
	 */
	@Override
	public int checkProductId(int id) {
		logger.info("Checking for the existence of product with ID: {}", id);
		boolean result = repo.existsById(id);
		if (result) {
			Product pro = repo.findByProductID(id);
			logger.info("Product with ID {} found. Current stock level is: {}", id, pro.getStockLevel());
			return pro.getStockLevel();
		}
		logger.warn("Product with ID {} not found.", id);
		return -1;
	}
}
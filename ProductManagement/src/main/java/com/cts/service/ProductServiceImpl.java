package com.cts.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

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
	 * the {@link ProductRepository}.
	 * </p>
	 */
	@Override
	public Product saveProduct(Product product) throws MethodArgumentNotValidException {
		StockDTO stockDto = new StockDTO();
		stockDto.setName(product.getName());
		stockDto.setProductID(product.getProductID());
		stockDto.setQuantity(product.getStockLevel());

		// Call the stock management microservice to create the stock entry
		sClient.save(stockDto);

		return repo.save(product);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation first checks if a product with the given ID exists. If it
	 * exists, the product's data is updated. If not, it throws a
	 * {@link ProductNotFound} exception to prevent creating a new product via the
	 * update endpoint.
	 * </p>
	 * * @throws ProductNotFound if no product with the specified ID is found.
	 */
	@Override
	public String updateProduct(Product product) throws MethodArgumentNotValidException, ProductNotFound {
		int productId = product.getProductID();

		if (repo.existsById(productId)) {

			repo.save(product);
			return "Product Updated Successfully";
		} else {

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
		Optional<Product> optional = repo.findById(id);
		if (optional.isEmpty()) {
			throw new ProductNotFound("Product with ID " + id + " not found.");
		} else {
			repo.deleteById(id);
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
		return repo.findByStockLevelGreaterThan(0);
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
		return repo.findAll();
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
		return repo.findByPriceBetween(initial, fina);
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
		boolean result = repo.existsById(id);
		if (result) {

			Product pr = repo.getReferenceById(id);
			return pr.getName();
		} else {
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
		List<OverAllStock> pr = repo.getAllStocks();
		return pr;

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
		List<ProductDTO> pr = repo.getAllProductQuantity();
		return pr;

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
		Product product = repo.findByProductID(quantityDTO.getProductID());

		product.setStockLevel(quantityDTO.getQuantity());
		repo.save(product);
		return "Successfully updated quantity";
	}

	@Override
	public int checkProductId(int id) {

		boolean result = repo.existsById(id);
		System.out.println(result);
		if (result) {
			Product pro = repo.findByProductID(id);
			return pro.getStockLevel();
		}
		return -1;
	}
}
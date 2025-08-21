package com.cts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.cts.exception.ProductNotFound;
import com.cts.model.OverAllStock;
import com.cts.model.Product;
import com.cts.model.ProductDTO;
import com.cts.model.QuantityDTO;
import com.cts.repository.ProductRepository;
import com.cts.service.ProductServiceImpl;

class UserServiceTest {

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private ProductServiceImpl service;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testAddproduct() throws MethodArgumentNotValidException {
		Product product = new Product(1, "Rice", "Cerial", 43, 50);
		when(productRepository.save(product)).thenReturn(null);

		Product result = service.saveProduct(product);

		assertEquals(result, "Product Saved Successfully");
	}

	@Test
	void updateProduct_throwsException_whenProductNotFound() {
		// Arrange
		int nonExistentProductId = 999;
		Product nonExistentProduct = new Product();
		nonExistentProduct.setProductID(nonExistentProductId);

		// Mock the behavior of the repository
		when(productRepository.existsById(nonExistentProductId)).thenReturn(false);

		// Act & Assert
		// Verify that calling updateProduct with a non-existent ID throws
		// ProductNotFound
		assertThrows(ProductNotFound.class, () -> {
			service.updateProduct(nonExistentProduct);
		});
	}

	@Test
	void updateProduct_shouldReturnSuccessMessage_whenProductExists() throws Exception {
		// Arrange
		Product product = new Product(1, "Updated Product", "Updated description", 20, 150);
		when(productRepository.existsById(1)).thenReturn(true);
		when(productRepository.save(any(Product.class))).thenReturn(product);

		// Act
		String result = service.updateProduct(product);

		// Assert
		assertEquals("Product Updated Successfully", result);
		verify(productRepository, times(1)).existsById(1);
		verify(productRepository, times(1)).save(product);
	}

	@Test
	void deleteProductById_shouldReturnSuccessMessage_whenProductExists() throws ProductNotFound {
		// Arrange
		int productId = 1;
		when(productRepository.findById(productId)).thenReturn(Optional.of(new Product()));

		// Act
		String result = service.deleteProductById(productId);

		// Assert
		assertEquals("Product deleted Successfully", result);
		verify(productRepository, times(1)).findById(productId);
		verify(productRepository, times(1)).deleteById(productId);
	}

	@Test
	void deleteProductById_shouldThrowException_whenProductDoesNotExist() {
		// Arrange
		int productId = 99;
		when(productRepository.findById(productId)).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(ProductNotFound.class, () -> service.deleteProductById(productId));
		verify(productRepository, times(1)).findById(productId);
		verify(productRepository, times(0)).deleteById(productId);
	}

	// --- getProductName tests ---
	@Test
	void getProductName_shouldReturnName_whenProductExists() throws ProductNotFound {
		// Arrange
		int productId = 1;
		Product product = new Product(productId, "Test Product", "desc", 10, 100);
		when(productRepository.existsById(productId)).thenReturn(true);
		when(productRepository.getReferenceById(productId)).thenReturn(product);

		// Act
		String result = service.getProductName(productId);

		// Assert
		assertEquals("Test Product", result);
		verify(productRepository, times(1)).existsById(productId);
		verify(productRepository, times(1)).getReferenceById(productId);
	}

	@Test
	void getProductName_shouldThrowException_whenProductDoesNotExist() {
		// Arrange
		int productId = 99;
		when(productRepository.existsById(productId)).thenReturn(false);

		// Act & Assert
		assertThrows(ProductNotFound.class, () -> service.getProductName(productId));
		verify(productRepository, times(1)).existsById(productId);
	}

	// --- getAllAvailableProducts tests ---
	@Test
	void getAllAvailableProducts_shouldReturnAvailableProducts() {
		// Arrange
		List<Product> availableProducts = Arrays.asList(new Product(1, "Available1", "", 5, 10),
				new Product(2, "Available2", "desc", 1, 20));
		when(productRepository.findByStockLevelGreaterThan(0)).thenReturn(availableProducts);

		// Act
		List<Product> result = service.getAllAvailableProducts();

		// Assert
		assertEquals(2, result.size());
		assertEquals("Available1", result.get(0).getName());
		verify(productRepository, times(1)).findByStockLevelGreaterThan(0);
	}

	@Test
	void getAllAvailableProducts_shouldReturnEmptyList_whenNoProductsAreAvailable() {
		// Arrange
		when(productRepository.findByStockLevelGreaterThan(0)).thenReturn(Collections.emptyList());

		// Act
		List<Product> result = service.getAllAvailableProducts();

		// Assert
		assertEquals(0, result.size());
		verify(productRepository, times(1)).findByStockLevelGreaterThan(0);
	}

	// --- getAllProducts tests ---
	@Test
	void getAllProducts_shouldReturnAllProducts() {
		// Arrange
		List<Product> allProducts = Arrays.asList(new Product(1, "Prod1", "", 5, 10), new Product(2, "Prod2", "", 0, 20) // Out
																															// of
																															// stock
		);
		when(productRepository.findAll()).thenReturn(allProducts);

		// Act
		List<Product> result = service.getAllProducts();

		// Assert
		assertEquals(2, result.size());
		assertEquals("Prod1", result.get(0).getName());
		verify(productRepository, times(1)).findAll();
	}

	// --- getProductsBetweenPriceRange tests ---
	@Test
	void getProductsBetweenPriceRange_shouldReturnCorrectProducts() {
		// Arrange
		List<Product> productsInRange = Arrays.asList(new Product(1, "In Range", "desc", 5, 50),
				new Product(2, "In Range", "desc", 10, 75));
		when(productRepository.findByPriceBetween(50, 100)).thenReturn(productsInRange);

		// Act
		List<Product> result = service.getProductsBetweenPriceRange(50, 100);

		// Assert
		assertEquals(2, result.size());
		assertEquals(5, result.get(0).getPrice());
		verify(productRepository, times(1)).findByPriceBetween(50, 100);
	}

	// --- getAllStocks tests ---
		@Test
		void getAllStocks_shouldReturnAllOverallStocks() {
			// Arrange
			List<OverAllStock> stocks = Arrays.asList(new OverAllStock(), new OverAllStock());
			when(productRepository.getAllStocks()).thenReturn(stocks);
	 
			// Act
			List<OverAllStock> result = service.getAllStocks();
	 
			// Assert
			assertEquals(2, result.size());
			assertEquals("Prod1", result.get(0).getProductID());
			verify(productRepository, times(1)).getAllStocks();
		}
	// --- getAllProductQuantity tests ---
	@Test
	void getAllProductQuantity_shouldReturnAllProductDTOs() {
		// Arrange
		List<ProductDTO> dtos = Arrays.asList(new ProductDTO(1, "Prod1", 10), new ProductDTO(2, "Prod2", 0));
		when(productRepository.getAllProductQuantity()).thenReturn(dtos);

		// Act
		List<ProductDTO> result = service.getAllProductQuantity();

		// Assert
		assertEquals(2, result.size());
		assertEquals("Prod1", result.get(0).getName());
		verify(productRepository, times(1)).getAllProductQuantity();
	}

	// --- updateQuantity tests ---
	@Test
	void updateQuantity_shouldUpdateProductStockLevel() {
		// Arrange
		QuantityDTO quantityDTO = new QuantityDTO(1, 25);
		Product existingProduct = new Product(1, "Test Product", "", 10, 100);
		when(productRepository.findByProductID(1)).thenReturn(existingProduct);
		when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

		// Act
		String result = service.updateQuantity(quantityDTO);

		// Assert
		assertEquals("Successfully updated quantity", result);
		assertEquals(25, existingProduct.getStockLevel());
		verify(productRepository, times(1)).findByProductID(1);
		verify(productRepository, times(1)).save(existingProduct);
	}
}

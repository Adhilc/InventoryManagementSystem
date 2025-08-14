package com.cts;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.cts.model.Product;
import com.cts.service.ProductService;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductManagementApplicationTests {

	@Autowired
	private ProductService productService;

	@BeforeAll
	void setupAll() {
		System.out.println("Starting integration tests...");
	}

	@BeforeEach
	void setup() throws MethodArgumentNotValidException {
		Product product = new Product(1, "Rice", "Cerial", 43, 50);
		productService.saveProduct(product);
	}

	@Test
	@Order(1)
	void testAddProduct1() throws MethodArgumentNotValidException {
		Product product = new Product(1, "Rice", "Cerial", 43, 50);
		String result = productService.saveProduct(product);
		assertEquals("Product Saved Successfully", result);
	}

	@Test
	@Order(2)
	void testAddProduct2() throws MethodArgumentNotValidException {
		Product product = new Product(2, "Phone", "Cerial", 43000, 43);
		String result = productService.saveProduct(product);
		assertEquals("Product Saved Successfully", result);
	}

}

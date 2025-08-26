package com.cts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.cts.client.ProductManagementClient;
import com.cts.client.StockManagementClient;
import com.cts.exception.DataNotFoundException;
import com.cts.exception.DateNotFoundException;
import com.cts.exception.OrderNotFoundException;
import com.cts.model.Order;
import com.cts.model.OrderReport;
import com.cts.model.Product;
import com.cts.model.ProductDTO;
import com.cts.model.Stock;
import com.cts.repository.OrderManagementRepository;
import com.cts.service.OrderManagementServiceImpl;

@ExtendWith(MockitoExtension.class)
class OrderManagementServiceImplTest {

    @Mock
    private OrderManagementRepository repo;

    @Mock
    private StockManagementClient sClient;
    
    @Mock
    private ProductManagementClient pClient;

    @InjectMocks
    private OrderManagementServiceImpl service;

    private Product product;
    private Order order;

    @BeforeEach
    void setUp() {
        // Initialize common test data
        product = new Product(1, 10);
        order = new Order(1, 101, 1, 10, LocalDate.now(), "Pending");
    }
    
    // Tests the successful creation of an order.
    @Test
    void testCreateOrder_Success() throws DataNotFoundException {
        // Arrange
        
        Stock stock = new Stock(product.getProductId(), 90, 10);
        ResponseEntity<Stock> stockResponse = new ResponseEntity<>(stock, HttpStatus.OK);
        
        // Mock the Feign client calls and repository save
        when(pClient.checkProductId(product.getProductId())).thenReturn(100); // Assume 100 items are available(quantity)
        when(sClient.decreaseStockFromOrder(any(ProductDTO.class))).thenReturn(stockResponse);
        when(repo.save(any(Order.class))).thenReturn(order);
        
        // Act
        ResponseEntity<String> response = service.createOrder(product);
        
        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Saved succesfully", response.getBody());
        
        // Verify that the external services and repository were called
        verify(pClient, times(1)).checkProductId(product.getProductId());
        verify(sClient, times(1)).decreaseStockFromOrder(any(ProductDTO.class));
        verify(repo, times(1)).save(any(Order.class));
    }

    // Tests that an exception is thrown for invalid product data.
    @Test
    void testCreateOrder_InvalidData_ThrowsException() {
        // Arrange
        Product invalidProduct = new Product(1, 0); // Invalid quantity
        
        // Act & Assert
        assertThrows(DataNotFoundException.class, () -> {
            service.createOrder(invalidProduct);
        });
        
        // Verify that no external calls were made
        verify(pClient, never()).checkProductId(any(Integer.class));
        verify(sClient, never()).decreaseStockFromOrder(any());
        verify(repo, never()).save(any());
    }
    
    // Tests that an exception is thrown when the product is not found.
    @Test
    void testCreateOrder_ProductNotFound_ThrowsException() {
        // Arrange
        when(pClient.checkProductId(product.getProductId())).thenReturn(-1);
        
        // Act & Assert
        assertThrows(DataNotFoundException.class, () -> {
            service.createOrder(product);
        });
    }
    
    // Tests that an exception is thrown when the available stock is insufficient.
    @Test
    void testCreateOrder_InsufficientStock_ThrowsException() {
        // Arrange
        when(pClient.checkProductId(product.getProductId())).thenReturn(5); // Only 5 in stock, but 10 are ordered
        
        // Act & Assert
        assertThrows(DataNotFoundException.class, () -> {
            service.createOrder(product);
        });
    }

    // Tests retrieving an order by its ID when it exists.
    @Test
    void testGetDetailsByOrderId_Found_ReturnsOrder() throws OrderNotFoundException {
        // Arrange
        when(repo.findByOrderId(order.getOrderId())).thenReturn(Optional.of(order));

        // Act
        ResponseEntity<Order> response = service.getDetailsByOrderId(order.getOrderId());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(order, response.getBody());
    }

    // Tests retrieving an order by an ID that does not exist.
    @Test
    void testGetDetailsByOrderId_NotFound_ThrowsException() {
        // Arrange
        when(repo.findByOrderId(99)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(OrderNotFoundException.class, () -> {
            service.getDetailsByOrderId(99);
        });
    }

    // Tests retrieving orders for a customer who has placed orders.
    @Test
    void testGetDetailsByCustomerId_Found_ReturnsOrderList() throws OrderNotFoundException {
        // Arrange
        List<Order> orders = Collections.singletonList(order);
        when(repo.findByCustomerId(order.getCustomerId())).thenReturn(orders);

        // Act
        ResponseEntity<List<Order>> response = service.getDetailsByCustomerId(order.getCustomerId());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orders, response.getBody());
    }

    // Tests retrieving orders for a customer ID that has no orders.
    @Test
    void testGetDetailsByCustomerId_NotFound_ThrowsException() {
        // Arrange
        when(repo.findByCustomerId(99)).thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(OrderNotFoundException.class, () -> {
            service.getDetailsByCustomerId(99);
        });
    }

    // Tests that an exception is thrown if the requested report date range is invalid.
    @Test
    void testGetDetailsByDate_RequestedDateOutsideAvailableRange_ThrowsException() {
        // Arrange
        Order oldestOrder = new Order();
        oldestOrder.setOrderDate(LocalDate.now().minusDays(10));
        Order newestOrder = new Order();
        newestOrder.setOrderDate(LocalDate.now().minusDays(5));
        
        when(repo.findFirstByOrderByOrderDateAsc()).thenReturn(Optional.of(oldestOrder));
        when(repo.findFirstByOrderByOrderDateDesc()).thenReturn(Optional.of(newestOrder));
        
        OrderReport reportRequest = new OrderReport(LocalDate.now().minusDays(20), LocalDate.now());

        // Act & Assert
        assertThrows(DateNotFoundException.class, () -> {
            service.getDetailsByDate(reportRequest);
        });
    }

    // Tests the successful update of an order's status.
    @Test
    void testUpdateStatus_Success() throws OrderNotFoundException {
        // Arrange
        Order existingOrder = new Order(1, 101, 1, 5, LocalDate.now(), "Pending");
        when(repo.findByOrderId(1)).thenReturn(Optional.of(existingOrder));

        // Act
        String result = service.updateStatus(1, "Shipped");

        // Assert
        assertEquals("Successfully Updated status", result);
        assertEquals("Shipped", existingOrder.getStatus());
        verify(repo, times(1)).save(existingOrder);
    }

    // Tests updating a status for an order that does not exist.
    @Test
    void testUpdateStatus_OrderNotFound_ThrowsException() {
        // Arrange
        when(repo.findByOrderId(99)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(OrderNotFoundException.class, () -> {
            service.updateStatus(99, "Shipped");
        });
        
        verify(repo, never()).save(any(Order.class));
    }
}
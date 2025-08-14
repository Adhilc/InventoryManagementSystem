package com.cts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
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

import com.cts.client.StockManagementClient;
import com.cts.exception.DataNotFoundException;
import com.cts.exception.DateNotFoundException;
import com.cts.exception.OrderNotFoundException;
import com.cts.model.Order;
import com.cts.model.OrderReport;
import com.cts.model.OrderReportSent;
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

    @InjectMocks
    private OrderManagementServiceImpl service;

    private Product product;
    private Order order;
    private OrderReport orderReport;

    @BeforeEach
    void setUp() {
        // Initialize common test data
        product = new Product(1, 10);
        // Corrected Order constructor call to match the entity's AllArgsConstructor
        // The constructor signature is:
        // public Order(int orderId, int customerId, int productId, int quantity, LocalDateTime orderDate, String status)
        order = new Order(1, 101, 1, 10, LocalDateTime.now(), "Pending");
        orderReport = new OrderReport(LocalDateTime.now().minusDays(1), LocalDateTime.now());
    }

    // --- createOrder Tests ---
    @Test
    void createOrder_Success() throws DataNotFoundException {
        // Mock external and repository calls
        when(sClient.decreaseStockFromOrder(any(ProductDTO.class)))
            .thenReturn(new ResponseEntity<>(new Stock(), HttpStatus.OK));
        when(repo.save(any(Order.class))).thenReturn(order);

        ResponseEntity<String> response = service.createOrder(product);

        // Verify outcomes
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Saved succesfully", response.getBody());
        verify(sClient, times(1)).decreaseStockFromOrder(any(ProductDTO.class));
        verify(repo, times(1)).save(any(Order.class));
    }

    @Test
    void createOrder_InvalidData_ThrowsException() {
        Product invalidProduct = new Product(0, -1);
        
        assertThrows(DataNotFoundException.class, () -> service.createOrder(invalidProduct));
        
        verify(sClient, never()).decreaseStockFromOrder(any());
        verify(repo, never()).save(any());
    }

    // --- getDetailsByOrderId Tests ---
    @Test
    void getDetailsByOrderId_Found_ReturnsOrder() throws OrderNotFoundException {
        when(repo.findByOrderId(order.getOrderId())).thenReturn(Optional.of(order));

        ResponseEntity<Order> response = service.getDetailsByOrderId(order.getOrderId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(order, response.getBody());
    }

    @Test
    void getDetailsByOrderId_NotFound_ThrowsException() {
        when(repo.findByOrderId(99)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> service.getDetailsByOrderId(99));
    }

    // --- getDetailsByCustomerId Tests ---
    @Test
    void getDetailsByCustomerId_Found_ReturnsOrderList() throws OrderNotFoundException {
        List<Order> orders = Collections.singletonList(order);
        when(repo.findByCustomerId(order.getCustomerId())).thenReturn(orders);

        ResponseEntity<List<Order>> response = service.getDetailsByCustomerId(order.getCustomerId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orders, response.getBody());
    }

    @Test
    void getDetailsByCustomerId_NotFound_ThrowsException() {
        when(repo.findByCustomerId(99)).thenReturn(Collections.emptyList());

        assertThrows(OrderNotFoundException.class, () -> service.getDetailsByCustomerId(99));
    }

   
    
    @Test
    void getDetailsByDate_RequestedDateOutsideAvailableRange_ThrowsException() {
        // Setup mock to return a limited date range
        Order oldestOrder = new Order();
        oldestOrder.setOrderDate(LocalDateTime.now().minusDays(10));
        
        Order newestOrder = new Order();
        newestOrder.setOrderDate(LocalDateTime.now().minusDays(5));

        when(repo.findFirstByOrderByOrderDateAsc()).thenReturn(Optional.of(oldestOrder));
        when(repo.findFirstByOrderByOrderDateDesc()).thenReturn(Optional.of(newestOrder));

        // Create a report request outside this range
        OrderReport report = new OrderReport(LocalDateTime.now().minusDays(20), LocalDateTime.now());

        assertThrows(DateNotFoundException.class, () -> service.getDetailsByDate(report));
    }

    // --- updateStatus Tests ---
    @Test
    void updateStatus_Success() throws OrderNotFoundException {
        Order existingOrder = new Order(1, 101, 1, 5, LocalDateTime.now(), "Pending");
        when(repo.findByOrderId(1)).thenReturn(Optional.of(existingOrder));

        String result = service.updateStatus(1, "Shipped");

        assertEquals("Successfully Updated status", result);
        verify(repo, times(1)).save(existingOrder);
        assertEquals("Shipped", existingOrder.getStatus());
    }

    @Test
    void updateStatus_OrderNotFound_ThrowsException() {
        when(repo.findByOrderId(99)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> service.updateStatus(99, "Shipped"));
        verify(repo, never()).save(any(Order.class));
    }
}
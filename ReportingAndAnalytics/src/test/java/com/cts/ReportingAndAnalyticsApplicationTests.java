package com.cts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.cts.controller.ReportingAndAnalyticsController;
import com.cts.model.OrderReport;
import com.cts.model.OrderReportSent;
import com.cts.model.OverAllStock;
import com.cts.model.StockDTO;
import com.cts.model.SupplierReport;
import com.cts.model.SupplierReportSent;
import com.cts.service.ReportingAndAnalyticsService;

/**
 * A simple unit test for the ReportingAndAnalyticsController.
 * This test uses only Mockito to test the controller as a plain Java object,
 * without loading a Spring context.
 */
@ExtendWith(MockitoExtension.class) // Enables Mockito for creating mocks
class ReportingAndAnalyticsApplicationTests {

    // @Mock creates a mock (fake version) of the service.
    @Mock
    private ReportingAndAnalyticsService service;

    // @InjectMocks creates an instance of our controller and injects the mock service into it.
    @InjectMocks
    private ReportingAndAnalyticsController controller;

    private OrderReportSent orderReportSent;

    @BeforeEach
    void setUp() {
        // Create some sample data to use in our tests.
        orderReportSent = new OrderReportSent();
        orderReportSent.setProductId(101);
        orderReportSent.setQuantity(50);
        orderReportSent.setDate(LocalDate.of(2025, 8, 26));
    }

    @Test
    @DisplayName("Should return order details from the service")
    void testGetOrderDetailsByDate() {
        // 1. Arrange: Tell our mock service what to return when its method is called.
        LocalDate startDate = LocalDate.of(2025, 8, 1);
        LocalDate endDate = LocalDate.of(2025, 8, 30);
        List<OrderReportSent> mockResponseList = Collections.singletonList(orderReportSent);
        when(service.getDetailsByDate(any(OrderReport.class)))
            .thenReturn(new ResponseEntity<>(mockResponseList, HttpStatus.OK));

        // 2. Act: Call the controller method directly.
        ResponseEntity<List<OrderReportSent>> response = controller.getOrderDetailsByDate(startDate, endDate);

        // 3. Assert: Check if the response from the controller is correct.
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(101, response.getBody().get(0).getProductId());
    }

    @Test
    @DisplayName("Should return supplier details from the service")
    void testGetSupplierDetailsByDate() {
        // 1. Arrange
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusHours(1);
        List<SupplierReportSent> mockResponseList = Collections.singletonList(new SupplierReportSent());
        when(service.getSupplierDetailsByDate(any(SupplierReport.class)))
            .thenReturn(new ResponseEntity<>(mockResponseList, HttpStatus.OK));

        // 2. Act
        ResponseEntity<List<SupplierReportSent>> response = controller.getSupplierDetailsByDate(startDate, endDate);

        // 3. Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Should return lower stocks from the service")
    void testGetTheLowerStocks() {
        // 1. Arrange
        List<StockDTO> mockResponseList = Collections.singletonList(new StockDTO());
        when(service.getTheLowerStocks()).thenReturn(new ResponseEntity<>(mockResponseList, HttpStatus.OK));

        // 2. Act
        ResponseEntity<List<StockDTO>> response = controller.getTheLowerStocks();

        // 3. Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Should return all stocks from the service")
    void testGetAllStocks() {
        // 1. Arrange
        List<OverAllStock> mockResponseList = Collections.singletonList(new OverAllStock());
        when(service.getAllStocks()).thenReturn(mockResponseList);

        // 2. Act
        List<OverAllStock> response = controller.getAllStocks();

        // 3. Assert
        assertNotNull(response);
        assertEquals(1, response.size());
    }
}
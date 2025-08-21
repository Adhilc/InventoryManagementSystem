package com.cts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.cts.client.OrderManagementClient;
import com.cts.client.ProductManagementClient;
import com.cts.client.StockManagementClient;
import com.cts.client.SupplierManagementClient;
import com.cts.model.OrderReport;
import com.cts.model.OrderReportSent;
import com.cts.model.OverAllStock;
import com.cts.model.StockDTO;
import com.cts.model.SupplierReport;
import com.cts.model.SupplierReportSent;
import com.cts.service.ReportingAndAnalyticsServiceImpl;

@SpringBootTest
class ReportingAndAnalyticsApplicationTests {

    
    @Mock
    private OrderManagementClient oClient;

    @Mock
    private SupplierManagementClient sClient;

    @Mock
    private StockManagementClient stClient;

    @Mock
    private ProductManagementClient pClient;

    // Inject mocks into the service class under test
    @InjectMocks
    private ReportingAndAnalyticsServiceImpl service;

    // --- Test Data Initialization ---
    private OrderReport orderReport;
    private SupplierReport supplierReport;
    private OrderReportSent orderReportSent;
    private SupplierReportSent supplierReportSent;
    private StockDTO stockDTO;
    private OverAllStock overAllStock;

    @BeforeEach
    void setUp() {
        // Initialize DTOs used for testing
        orderReport = new OrderReport();
        orderReport.setStartDate(LocalDate.now());
        orderReport.setEndDate(LocalDate.now());

        supplierReport = new SupplierReport();
        supplierReport.setStartDate(LocalDateTime.now());
        supplierReport.setEndDate(LocalDateTime.now());

        orderReportSent = new OrderReportSent();
        orderReportSent.setProductId(1);
        orderReportSent.setQuantity(10);
        orderReportSent.setDate(LocalDate.now());

        supplierReportSent = new SupplierReportSent();
        stockDTO = new StockDTO();
        overAllStock = new OverAllStock();
    }

    // --- Test Methods ---

    @Test
    void contextLoads() {
        // This test ensures the Spring application context loads correctly.
        // No additional code should be added here.
    }

    @Test
    void getDetailsByDate_Success() {
        // Mock the Feign client's successful response
        when(oClient.getDetailsByDate(any(OrderReport.class)))
            .thenReturn(new ResponseEntity<>(Collections.singletonList(orderReportSent), HttpStatus.OK));

        // Call the service method and assert the result
        ResponseEntity<List<OrderReportSent>> response = service.getDetailsByDate(orderReport);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(orderReportSent, response.getBody().get(0));

        // Verify the mock client was called exactly once
        verify(oClient, times(1)).getDetailsByDate(any(OrderReport.class));
    }

    @Test
    void getSupplierDetailsByDate_Success() {
        // Mock the Feign client's successful response
        when(sClient.getSupplierInfoForReport(any(SupplierReport.class)))
            .thenReturn(new ResponseEntity<>(Collections.singletonList(supplierReportSent), HttpStatus.OK));

        // Call the service method and assert the result
        ResponseEntity<List<SupplierReportSent>> response = service.getSupplierDetailsByDate(supplierReport);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(supplierReportSent, response.getBody().get(0));

        // Verify the mock client was called
        verify(sClient, times(1)).getSupplierInfoForReport(any(SupplierReport.class));
    }

    @Test
    void getTheLowerStocks_Success() {
        // Mock the Feign client's successful response
        when(stClient.getLowStockReport())
            .thenReturn(new ResponseEntity<>(Collections.singletonList(stockDTO), HttpStatus.OK));

        // Call the service method and assert the result
        ResponseEntity<List<StockDTO>> response = service.getTheLowerStocks();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(stockDTO, response.getBody().get(0));

        // Verify the mock client was called
        verify(stClient, times(1)).getLowStockReport();
    }

    @Test
    void getAllStocks_Success() {
        // Mock the Feign client's successful response
        when(pClient.getAllProductsStocks())
            .thenReturn(Collections.singletonList(overAllStock));

        // Call the service method and assert the result
        List<OverAllStock> response = service.getAllStocks();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(overAllStock, response.get(0));

        // Verify the mock client was called
        verify(pClient, times(1)).getAllProductsStocks();
    }
}
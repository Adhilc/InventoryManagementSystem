package com.cts.StockManagementService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cts.client.ProductManagementClient;
import com.cts.stockmanagementexceptions.InsufficientStockException;
import com.cts.stockmanagementexceptions.InvalidStockAmountException;
import com.cts.stockmanagementexceptions.StockNotFoundException;
import com.cts.stockmanagementmodel.Stock;
import com.cts.stockmanagementmodel.StockDTO;
import com.cts.stockmanagementrepository.StockManagementRepository;
import com.cts.stockmanagementservice.StockManagementServiceImpl;

@ExtendWith(MockitoExtension.class)
class StockManagementServiceImplTest {

    // Creates a mock instance of the repository.
    @Mock
    private StockManagementRepository stockRepository;

    // Mocks the Feign client dependency.
    @Mock
    private ProductManagementClient pClient;

    // Injects the mock repository and client into the service implementation.
    @InjectMocks
    private StockManagementServiceImpl stockManagementService;

    private Stock testStock;

    // This method runs before each test to set up a clean test object.
    @BeforeEach
    void setUp() {
        // The Stock constructor now includes 'name'
        testStock = new Stock(101, "Test Product A", 50, 10);
    }

    // --- Tests for getStockByProductId ---

    @Test
    void whenGetStockByProductId_andStockExists_thenReturnStock() {
        // Arrange: Configure the mock repository to return our test stock.
        when(stockRepository.findById(101)).thenReturn(Optional.of(testStock));

        // Act: Call the service method.
        Stock foundStock = stockManagementService.getStockByProductId(101);

        // Assert: Check that the correct stock was returned.
        assertNotNull(foundStock);
        assertEquals(101, foundStock.getProductID());
    }

    @Test
    void whenGetStockByProductId_andStockDoesNotExist_thenThrowStockNotFoundException() {
        // Arrange: Configure the mock to return an empty Optional.
        when(stockRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act & Assert: Verify that the correct exception is thrown.
        assertThrows(StockNotFoundException.class, () -> {
            stockManagementService.getStockByProductId(999);
        });
    }

    // --- Tests for increaseStock ---

    @Test
    void whenIncreaseStock_withValidAmount_thenIncreaseQuantity() {
        when(stockRepository.findById(101)).thenReturn(Optional.of(testStock));
        when(stockRepository.save(any(Stock.class))).thenReturn(testStock);

        Stock updatedStock = stockManagementService.increaseStock(101, 10);

        assertNotNull(updatedStock);
        assertEquals(60, updatedStock.getQuantity()); // 50 + 10
        verify(stockRepository, times(1)).save(testStock); // Verify save was called
    }

    @Test
    void whenIncreaseStock_withInvalidAmount_thenThrowInvalidStockAmountException() {
        assertThrows(InvalidStockAmountException.class, () -> {
            stockManagementService.increaseStock(101, -5);
        });
    }

    // --- Tests for decreaseStock ---

    @Test
    void whenDecreaseStock_withSufficientStock_thenDecreaseQuantity() {
        when(stockRepository.findById(101)).thenReturn(Optional.of(testStock));
        when(stockRepository.save(any(Stock.class))).thenReturn(testStock);

        Stock updatedStock = stockManagementService.decreaseStock(101, 20);

        assertNotNull(updatedStock);
        assertEquals(30, updatedStock.getQuantity()); // 50 - 20
        verify(stockRepository, times(1)).save(testStock);
    }

    @Test
    void whenDecreaseStock_withInsufficientStock_thenThrowInsufficientStockException() {
        when(stockRepository.findById(101)).thenReturn(Optional.of(testStock));

        assertThrows(InsufficientStockException.class, () -> {
            stockManagementService.decreaseStock(101, 100); // Trying to decrease by more than available
        });
    }

    @Test
    void whenDecreaseStock_withInvalidAmount_thenThrowInvalidStockAmountException() {
        assertThrows(InvalidStockAmountException.class, () -> {
            stockManagementService.decreaseStock(101, 0);
        });
    }

    // --- Test for getLowStockItems ---

    @Test
    void whenGetLowStockItems_thenReturnLowStockList() {
        // Arrange: The repository now returns a List<Stock>
        Stock lowStockItem = new Stock(102, "Test Product B", 5, 10); // quantity is less than reorderLevel
        List<Stock> lowStockList = Collections.singletonList(lowStockItem);
        when(stockRepository.findLowStockItems()).thenReturn(lowStockList);

        // Act
        List<Stock> result = stockManagementService.getLowStockItems();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(102, result.get(0).getProductID());
    }

    // --- Test for sendLowStockItems ---

    @Test
    void whenSendLowStockItems_thenReturnListOfStockDTO() {
        // Arrange: The repository now returns a List<StockDTO>
        StockDTO lowStockDTO = new StockDTO(120,"Test Product C", 5); 
        List<StockDTO> lowStockDTOList = Collections.singletonList(lowStockDTO);
        
        // Mock the call to the repository method
        when(stockRepository.sendLowStockItems()).thenReturn(lowStockDTOList);

        // Act
        List<StockDTO> result = stockManagementService.sendLowStockItems();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Product C", result.get(0).getName());
        assertEquals(5, result.get(0).getQuantity());
        
        // Verify that the correct repository method was called
        verify(stockRepository, times(1)).sendLowStockItems();
    }

}
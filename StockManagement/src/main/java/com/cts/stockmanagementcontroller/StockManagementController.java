package com.cts.stockmanagementcontroller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.stockmanagementmodel.ProductDTO;
import com.cts.stockmanagementmodel.Stock;
import com.cts.stockmanagementmodel.StockDTO;
import com.cts.stockmanagementservice.StockManagementService;

import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for managing stock operations.
 * Handles all HTTP requests related to stock levels.
 */
@RestController
@RequestMapping("/api/stock") // Base path for all endpoints in this controller
@Slf4j // Lombok annotation to auto-generate a logger instance
public class StockManagementController {

    // The service layer that contains the business logic for stock management
    private final StockManagementService stockManagementService;

    /**
     * Constructor for dependency injection of the StockManagementService.
     * @param stockManagementService The service layer for stock management logic.
     */
    public StockManagementController(StockManagementService stockManagementService) {
        this.stockManagementService = stockManagementService;
    }

    /**
     * Creates a new stock entry based on the provided DTO.
     * @param stockDto The stock data transfer object from the request body.
     * @return A simple confirmation message.
     */
    @PostMapping("/save")
    public String saveStock(@RequestBody StockDTO stockDto) {
        log.info("Entering saveStock to create a new stock entry for product ID: {}", stockDto.getProductID());
        stockManagementService.saveStock(stockDto);
        log.info("Successfully saved new stock entry for product ID: {}", stockDto.getProductID());
        return "Saved successfully";
    }

    /**
     * Retrieves the stock information for a specific product by its ID.
     * @param productId The unique ID of the product.
     * @return A ResponseEntity containing the Stock object.
     */
    @GetMapping("/{productId}")
    public ResponseEntity<Stock> getStockByProductId(@PathVariable int productId) {
        log.info("Entering getStockByProductId for productId: {}", productId);
        Stock stock = stockManagementService.getStockByProductId(productId);
        log.info("Successfully retrieved stock for productId: {}. Current quantity: {}", productId, stock.getQuantity());
        return ResponseEntity.ok(stock);
    }

    /**
     * Increases the stock quantity for a specific product.
     * @param productId The ID of the product to update.
     * @param request A map containing the "amount" to increase the stock by.
     * @return A ResponseEntity with the updated Stock object.
     */
    @PutMapping("/{productId}/increase")
    public ResponseEntity<Stock> increaseStock(@PathVariable int productId, @RequestBody Map<String, Integer> request) {
        int amount = request.get("amount");
        log.info("Entering increaseStock for productId: {} with amount: {}", productId, amount);
        Stock updatedStock = stockManagementService.increaseStock(productId, amount);
        log.info("Successfully increased stock for productId: {}. New quantity: {}", productId, updatedStock.getQuantity());
        return ResponseEntity.ok(updatedStock);
    }

    /**
     * Decreases stock based on a product order DTO.
     * @param productDTO The product data transfer object from an order.
     * @return A ResponseEntity with the updated Stock object.
     */
    @PostMapping("/decrease")
    public ResponseEntity<Stock> decreaseStockFromOrder(@RequestBody ProductDTO productDTO) {
        log.info("Entering decreaseStockFromOrder for productId: {} with quantity: {}", productDTO.getProductID(), productDTO.getQuantity());
        Stock updatedStock = stockManagementService.decreaseStock(productDTO.getProductID(), productDTO.getQuantity());
        log.info("Successfully decreased stock from order for productId: {}. New quantity: {}", productDTO.getProductID(), updatedStock.getQuantity());
        return ResponseEntity.ok(updatedStock);
    }

    /**
     * Decreases the stock quantity for a specific product.
     * @param productId The ID of the product to update.
     * @param request A map containing the "amount" to decrease the stock by.
     * @return A ResponseEntity with the updated Stock object.
     */
    @PutMapping("/{productId}/decrease")
    public ResponseEntity<Stock> decreaseStock(@PathVariable int productId, @RequestBody Map<String, Integer> request) {
        int amount = request.get("amount");
        log.info("Entering decreaseStock for productId: {} with amount: {}", productId, amount);
        Stock updatedStock = stockManagementService.decreaseStock(productId, amount);
        log.info("Successfully decreased stock for productId: {}. New quantity: {}", productId, updatedStock.getQuantity());
        return ResponseEntity.ok(updatedStock);
    }

    /**
     * Generates a report of all items with low stock levels.
     * @return A ResponseEntity containing a list of low-stock Stock objects.
     */
    @GetMapping("/low-stock-report")
    public ResponseEntity<List<Stock>> getLowStockReport() {
        log.info("Entering getLowStockReport to fetch all low stock items.");
        List<Stock> lowStockItems = stockManagementService.getLowStockItems();
        log.info("Successfully retrieved low stock report. Found {} items.", lowStockItems.size());
        return ResponseEntity.ok(lowStockItems);
    }

    /**
     * Generates a report of low stock items, formatted as DTOs.
     * This is typically used for sending data to other services or a UI.
     * @return A ResponseEntity containing a list of low-stock StockDTOs.
     */
    @GetMapping("/send-stock-report")
    public ResponseEntity<List<StockDTO>> sendLowStockReport() {
        log.info("Entering sendLowStockReport to fetch low stock items as DTOs.");
        // The service layer method already returns the correct DTO list.
        List<StockDTO> lowStockDTOs = stockManagementService.sendLowStockItems();
        log.info("Successfully retrieved low stock DTO report. Found {} items.", lowStockDTOs.size());
        // Return the ResponseEntity with the list of DTOs.
        return ResponseEntity.ok(lowStockDTOs);
    }
}
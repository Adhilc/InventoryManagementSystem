package com.cts.stockmanagementservice;

import java.util.List;

import com.cts.stockmanagementmodel.Stock;
import com.cts.stockmanagementmodel.StockDTO;

/**
 * Defines the contract for stock management operations.
 * This interface outlines the core business logic for handling stock levels,
 * such as creating, retrieving, increasing, and decreasing stock quantities.
 */
public interface StockManagementService {

    /**
     * Creates and saves a new stock record based on the provided data.
     * @param stockDto A DTO containing the initial data for the new stock item.
     * @return A confirmation message indicating the result of the save operation.
     */
    public String saveStock(StockDTO stockDto);

    /**
     * Retrieves a specific stock item by its unique product ID.
     * @param productId The unique identifier for the product.
     * @return The {@link Stock} entity corresponding to the product ID.
     */
    public Stock getStockByProductId(int productId);

    /**
     * Increases the quantity of an existing stock item by a specified amount.
     * @param productId The ID of the product whose stock will be increased.
     * @param amount The amount to add to the current stock quantity.
     * @return The updated {@link Stock} entity with the new quantity.
     */
    public Stock increaseStock(int productId, int amount);

    /**
     * Decreases the quantity of an existing stock item by a specified amount.
     * @param productId The ID of the product whose stock will be decreased.
     * @param amount The amount to subtract from the current stock quantity.
     * @return The updated {@link Stock} entity with the new quantity.
     */
    public Stock decreaseStock(int productId, int amount);

    /**
     * Generates a report of all stock items that have fallen to or below their reorder level.
     * @return A list of {@link Stock} entities that are considered low in stock.
     */
    public List<Stock> getLowStockItems();

    /**
     * Generates a report of low stock items, formatted as DTOs for external use.
     * @return A list of {@link StockDTO} objects representing low stock items.
     */
    public List<StockDTO> sendLowStockItems();
}
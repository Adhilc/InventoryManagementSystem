package com.cts.stockmanagementservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.client.ProductManagementClient;
import com.cts.stockmanagementexceptions.InsufficientStockException;
import com.cts.stockmanagementexceptions.InvalidStockAmountException;
import com.cts.stockmanagementexceptions.StockNotFoundException;
import com.cts.stockmanagementmodel.QuantityDTO;
import com.cts.stockmanagementmodel.Stock;
import com.cts.stockmanagementmodel.StockDTO;
import com.cts.stockmanagementrepository.StockManagementRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StockManagementServiceImpl implements StockManagementService {

    private final StockManagementRepository stockRepository;
    private final ProductManagementClient pClient;

    @Autowired
    public StockManagementServiceImpl(StockManagementRepository stockRepository, ProductManagementClient pClient) {
        this.stockRepository = stockRepository;
        this.pClient = pClient;
    }

    /**
     * Retrieves a stock item by its product ID.
     * @param productId The ID of the product to find stock for.
     * @return The found Stock entity.
     * @throws StockNotFoundException if no stock is found for the given ID.
     */
    @Override
    public Stock getStockByProductId(int productId) {
        log.info("Attempting to find stock for product ID: {}", productId);
        // Find stock by ID or throw an exception if not found.
        return stockRepository.findById(productId)
                .orElseThrow(() -> {
                    log.warn("StockNotFoundException: No stock found for product ID: {}", productId);
                    return new StockNotFoundException("Stock not found for product ID: " + productId);
                });
    }

    /**
     * Increases the quantity of a stock item.
     * @param productId The ID of the product to update.
     * @param amount The positive amount to increase the stock by.
     * @return The updated Stock entity.
     */
    @Override
    public Stock increaseStock(int productId, int amount) {
        log.info("Service: Attempting to increase stock for product ID: {} by amount: {}", productId, amount);
        // Step 1: Validate that the increase amount is a positive number.
        if (amount <= 0) {
            log.warn("InvalidStockAmountException: Increase amount must be positive. Received: {}", amount);
            throw new InvalidStockAmountException("Increase amount must be positive.");
        }
        
        // Step 2: Get the current stock record from the database.
        Stock stock = getStockByProductId(productId);
        int originalQuantity = stock.getQuantity();
        int updatedQuantity = originalQuantity + amount;
        
        // Step 3: Prepare a Data Transfer Object (DTO) to communicate with the external Product service.
        QuantityDTO quantityDTO = new QuantityDTO();
        quantityDTO.setQuantity(updatedQuantity);
        quantityDTO.setProductID(productId);
        
        // Step 4: Update the quantity on the local entity object.
        stock.setQuantity(updatedQuantity);
        
        // Step 5: Call the external Product Management service via Feign client to keep quantities in sync.
        log.info("Calling ProductManagementClient to update quantity for product ID: {}", productId);
        pClient.updateQuantity(quantityDTO); // This is a synchronous (blocking) network call.
        log.info("ProductManagementClient call successful. Saving updated stock to repository.");
        
        // Step 6: Save the updated stock entity to the local stock database.
        Stock savedStock = stockRepository.save(stock); // Persist the changes.
        log.info("Successfully increased stock for product ID: {}. Original quantity: {}, New quantity: {}", productId, originalQuantity, savedStock.getQuantity());
        return savedStock;
    }

    /**
     * Decreases the quantity of a stock item.
     * @param productId The ID of the product to update.
     * @param amount The positive amount to decrease the stock by.
     * @return The updated Stock entity.
     * @throws InsufficientStockException if the amount to decrease is greater than the available quantity.
     */
    @Override
    public Stock decreaseStock(int productId, int amount) {
        log.info("Service: Attempting to decrease stock for product ID: {} by amount: {}", productId, amount);
        // Step 1: Validate that the decrease amount is a positive number.
        if (amount <= 0) {
            log.warn("InvalidStockAmountException: Decrease amount must be positive. Received: {}", amount);
            throw new InvalidStockAmountException("Decrease amount must be positive.");
        }
        
        // Step 2: Get the current stock record from the database.
        Stock stock = getStockByProductId(productId);
        int originalQuantity = stock.getQuantity();
        
        // Step 3: Check if there is enough stock to fulfill the decrease request.
        if (originalQuantity < amount) {
            log.warn("InsufficientStockException: Not enough stock for product ID: {}. Available: {}, Required: {}", productId, originalQuantity, amount);
            throw new InsufficientStockException("Insufficient stock for product ID: " + productId
                    + ". Available: " + originalQuantity + ", Required: " + amount);
        }
        
        // Step 4: Calculate the new quantity.
        int updatedQuantity = originalQuantity - amount;
        
        // Step 5: Prepare a DTO for the external Product service call.
        QuantityDTO quantityDTO = new QuantityDTO();
        quantityDTO.setQuantity(updatedQuantity);
        quantityDTO.setProductID(productId);
        
        // Step 6: Call the external Product Management service to sync the quantity.
        log.info("Calling ProductManagementClient to update quantity for product ID: {}", productId);
        pClient.updateQuantity(quantityDTO); // Blocking network call.
        log.info("ProductManagementClient call successful. Saving updated stock to repository.");
        
        // Step 7: Set the new quantity and save the entity to the local database.
        stock.setQuantity(updatedQuantity);
        Stock savedStock = stockRepository.save(stock); // Persist the changes.
        log.info("Successfully decreased stock for product ID: {}. Original quantity: {}, New quantity: {}", productId, originalQuantity, savedStock.getQuantity());
        return savedStock;
    }

    /**
     * Retrieves a list of all stock items that are at or below the reorder level.
     * @return A list of low stock items.
     */
    @Override
    public List<Stock> getLowStockItems() {
        log.info("Service: Fetching low stock items from repository.");
        // This call executes the custom JPQL query defined in the repository.
        List<Stock> lowStockItems = stockRepository.findLowStockItems(); 
        log.info("Found {} low stock items.", lowStockItems.size());
        return lowStockItems;
    }

    /**
     * Retrieves a list of low stock items as DTOs.
     * @return A list of StockDTOs for low stock items.
     */
    @Override
    public List<StockDTO> sendLowStockItems() {
        log.info("Service: Fetching low stock items as DTOs from repository.");
        // This call executes the custom JPQL query with a constructor expression.
        List<StockDTO> lowStockDTOs = stockRepository.sendLowStockItems(); 
        log.info("Found {} low stock items (DTOs).", lowStockDTOs.size());
        return lowStockDTOs;
    }
    
    /**
     * Creates and saves a new stock item from a DTO.
     * @param stockDto The DTO containing the new stock item's data.
     * @return A confirmation message.
     */
    public String saveStock(StockDTO stockDto) {
        log.info("Service: Saving new stock entry for product ID: {}", stockDto.getProductID());
        // Create a new Stock entity from the incoming DTO.
        Stock stock = new Stock();
        stock.setProductID(stockDto.getProductID());
        stock.setName(stockDto.getName());
        stock.setQuantity(stockDto.getQuantity());
        stock.setReorderLevel(20); // Set a default reorder level for new items.
        
        // Save the new entity to the database.
        stockRepository.save(stock);
        log.info("Successfully saved new stock for product ID: {}", stockDto.getProductID());
        return "saved";
    }
}

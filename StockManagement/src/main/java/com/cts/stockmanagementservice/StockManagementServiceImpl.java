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

    @Override
    public Stock getStockByProductId(int productId) {
        return stockRepository.findById(productId)
                .orElseThrow(() -> new StockNotFoundException("Stock not found for product ID: " + productId));
    }

    @Override
    public Stock increaseStock(int productId, int amount) {
        if (amount <= 0) {
            throw new InvalidStockAmountException("Increase amount must be positive.");
        }
        
        Stock stock = getStockByProductId(productId);
        QuantityDTO quantityDTO = new QuantityDTO();
        int updatedQuantity=stock.getQuantity() + amount;
        quantityDTO.setQuantity(updatedQuantity);
        quantityDTO.setProductID(productId);
        stock.setQuantity(updatedQuantity);
        pClient.updateQuantity(quantityDTO); // Blocking call
        return stockRepository.save(stock); // Blocking call
    }

    @Override
    public Stock decreaseStock(int productId, int amount) {
    	log.info("Input Data in service: "+productId+" and "+amount);
        if (amount <= 0) {
            throw new InvalidStockAmountException("Decrease amount must be positive.");
        }
        Stock stock = getStockByProductId(productId);
        if (stock.getQuantity() < amount) {
            throw new InsufficientStockException("Insufficient stock for product ID: " + productId
                    + ". Available: " + stock.getQuantity() + ", Required: " + amount);
        }
        QuantityDTO quantityDTO = new QuantityDTO();
       int updatedQuantity= stock.getQuantity() - amount;
        quantityDTO.setQuantity(updatedQuantity);
        quantityDTO.setProductID(productId);
    	log.info("Quantity DTO Data *******************************"+quantityDTO);
        pClient.updateQuantity(quantityDTO); // Blocking call
        log.info("After Open Feign...................");
        stock.setQuantity(updatedQuantity);
        return stockRepository.save(stock); // Blocking call
    }

    @Override
    public List<Stock> getLowStockItems() {
        return stockRepository.findLowStockItems(); // Blocking call
    }
    @Override
    public List<StockDTO> sendLowStockItems() {
        return stockRepository.sendLowStockItems(); // Blocking call
    }
    
    
    public String saveStock(StockDTO stockDto) {
    	
    	Stock stock = new Stock();
    	stock.setProductID(stockDto.getProductID());
    	stock.setName(stockDto.getName());
    	stock.setQuantity(stockDto.getQuantity());
    	stock.setReorderLevel(20);
       	stockRepository.save(stock);
    	return "saved";
    }


}
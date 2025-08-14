package com.cts.stockmanagementservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.client.ProductManagementClient;
import com.cts.stockmanagementexceptions.InsufficientStockException;
import com.cts.stockmanagementexceptions.InvalidStockAmountException;
import com.cts.stockmanagementexceptions.StockNotFoundException;
import com.cts.stockmanagementmodel.OverAllStock;
import com.cts.stockmanagementmodel.QuantityDTO;
import com.cts.stockmanagementmodel.Stock;
import com.cts.stockmanagementmodel.StockDTO;
import com.cts.stockmanagementrepository.StockManagementRepository;

@Service
public class StockManagementServiceImpl implements StockManagementService {

    private  StockManagementRepository stockRepository;
    private  ProductManagementClient pClient;
    @Autowired
    public StockManagementServiceImpl(StockManagementRepository stockRepository,ProductManagementClient pClient) {
        this.stockRepository = stockRepository;
        this. pClient=pClient;
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
        QuantityDTO quantityDTO =new QuantityDTO();
        quantityDTO.setQuantity(stock.getQuantity() + amount);
        String response=pClient.updateQuantity(quantityDTO);
        stock.setQuantity(stock.getQuantity() + amount);
        return stockRepository.save(stock);
    }

    @Override
    public Stock decreaseStock(int productId, int amount) {
        if (amount <= 0) {
            throw new InvalidStockAmountException("Decrease amount must be positive.");
        }
        Stock stock = getStockByProductId(productId);
        if (stock.getQuantity() < amount) {
            throw new InsufficientStockException("Insufficient stock for product ID: " + productId
                    + ". Available: " + stock.getQuantity() + ", Required: " + amount);
        }
        QuantityDTO quantityDTO =new QuantityDTO();
        quantityDTO.setQuantity(stock.getQuantity() - amount);
        String response=pClient.updateQuantity(quantityDTO);
        stock.setQuantity(stock.getQuantity() - amount);
        return stockRepository.save(stock);
    }

    @Override
    public List<Stock> getLowStockItems() {
        return stockRepository.findLowStockItems();
    }
    @Override
    public List<StockDTO> sendLowStockItems(){
    	return stockRepository.sendLowStockItems();
    }

    @Override
    public String save() {
    	
    	List<OverAllStock> overAllStocks = pClient.getAllProductsStocks();
    	for(OverAllStock overAllStock:overAllStocks) {	
    		Stock stock=new Stock();
    		stock.setProductID(overAllStock.getProductID());
    		stock.setName(overAllStock.getName());
    		stock.setQuantity(overAllStock.getQuantity());
    		stock.setReorderLevel(20);
    		stockRepository.save(stock);
    	}
    	return "saved successfully";
    }

	

	

 
}
package com.cts.stockmanagementcontroller;
 
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
 
@RestController
@RequestMapping("/api/stock")
public class StockManagementController {
 
    private final StockManagementService stockManagementService;
 
    @Autowired
    public StockManagementController(StockManagementService stockManagementService) {
        this.stockManagementService = stockManagementService;
    }
    
    @PostMapping("/save")
    public String save() {
    	stockManagementService.save();
    	return "Saved successfully";
    	
    	
    }
    @GetMapping("/{productId}")
    public ResponseEntity<Stock> getStockByProductId(@PathVariable int productId) {
        Stock stock = stockManagementService.getStockByProductId(productId);
        return ResponseEntity.ok(stock);
    }
 
 
   
    @PutMapping("/{productId}/increase")
    public ResponseEntity<Stock> increaseStock(@PathVariable int productId, @RequestBody Map<String, Integer> request) {
        int amount = request.get("amount");
        Stock updatedStock = stockManagementService.increaseStock(productId, amount);
        return ResponseEntity.ok(updatedStock);
    }
    @PostMapping("decrease")
    public ResponseEntity<Stock> decreaseStockFromOrder(@RequestBody ProductDTO productDTO) {
        Stock updatedStock = stockManagementService.decreaseStock(productDTO.getProductID(), productDTO.getQuantity());
        return ResponseEntity.ok(updatedStock);
    }
    
    
    @PutMapping("/{productId}/decrease")
    public ResponseEntity<Stock> decreaseStock(@PathVariable int productId, @RequestBody Map<String, Integer> request) {
        int amount = request.get("amount");
        Stock updatedStock = stockManagementService.decreaseStock(productId, amount);
        return ResponseEntity.ok(updatedStock);
    }
 
  
    @GetMapping("/low-stock-report")
    public ResponseEntity<List<Stock>> getLowStockReport() {
        List<Stock> lowStockItems = stockManagementService.getLowStockItems();
        return ResponseEntity.ok(lowStockItems);
    }
    
    
    @GetMapping("/send-stock-report")
    public ResponseEntity<List<StockDTO>> sendLowStockReport() {
        // 1. Call the service to get the raw list of Stock entities
        List<StockDTO> lowStockItems = stockManagementService.sendLowStockItems();
        
        // 2. Map the List<Stock> to a List<StockDTO>
        // This is the key step to resolve the type mismatch.
        List<StockDTO> lowStockDTOs = lowStockItems.stream()
                .map(stock -> new StockDTO(stock.getProductID(),  stock.getName(), stock.getQuantity()))
                .toList();
        
        // 3. Return the ResponseEntity with the new list of DTOs
        return ResponseEntity.ok(lowStockDTOs);
    }
}
 
 
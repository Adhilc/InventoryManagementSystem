package com.cts.stockmanagementservice;
 
import java.util.List;
 
import com.cts.stockmanagementmodel.Stock;
import com.cts.stockmanagementmodel.StockDTO;
 
public interface StockManagementService {
	public String saveStock(StockDTO stockDto);
//	public String save();
	public Stock getStockByProductId(int  productId);
    public Stock increaseStock(int productId, int amount);
    public Stock decreaseStock(int productId, int amount);
    public List<Stock> getLowStockItems();
    public List<StockDTO> sendLowStockItems();

}
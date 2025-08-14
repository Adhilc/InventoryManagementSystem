package com.cts.stockmanagementrepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cts.stockmanagementmodel.Stock;
import com.cts.stockmanagementmodel.StockDTO;

@Repository
public interface StockManagementRepository extends JpaRepository<Stock, Integer>{
	
	@Query("SELECT s FROM Stock s WHERE s.quantity <= 20000")
    public List<Stock> findLowStockItems();
	
	@Query("SELECT new com.cts.stockmanagementmodel.StockDTO(s.productID, s.name, s.quantity) FROM Stock s WHERE s.quantity <= 200000")
	public List<StockDTO> sendLowStockItems();
	
	

	
}

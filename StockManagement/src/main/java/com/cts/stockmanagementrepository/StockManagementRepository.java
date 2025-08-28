package com.cts.stockmanagementrepository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.cts.stockmanagementmodel.Stock;
import com.cts.stockmanagementmodel.StockDTO;
// Marks this interface as a Spring Data repository bean.
@Repository
// Repository for Stock entity, providing CRUD operations.
public interface StockManagementRepository extends JpaRepository<Stock, Integer>{
	// Custom query to find all stock items with quantity less than or equal to 20.
	@Query("SELECT s FROM Stock s WHERE s.quantity <= 20")
    public List<Stock> findLowStockItems();
	// Custom query to find low stock items and return them as a list of StockDTOs.
	@Query("SELECT new com.cts.stockmanagementmodel.StockDTO(s.productID, s.name, s.quantity) FROM Stock s WHERE s.quantity <= 20")
	public List<StockDTO> sendLowStockItems();

}
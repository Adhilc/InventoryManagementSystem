package com.cts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cts.model.OverAllStock;
import com.cts.model.Product;
import com.cts.model.ProductDTO;

/**
 * Spring Data JPA repository for the {@link Product} entity. 🗄️
 * <p>
 * This interface extends {@link JpaRepository}, which provides a rich set of
 * standard CRUD (Create, Read, Update, Delete) and pagination methods for the
 * {@code Product} entity out of the box. The primary key type for the Product
 * is {@code Integer}.
 * </p>
 * Custom queries can be defined by declaring method signatures.
 */
public interface ProductRepository extends JpaRepository<Product, Integer> {

	/**
	 * Finds all products that have a stock level greater than the specified value.
	 * <p>
	 * This is a derived query method; Spring Data JPA will automatically generate
	 * the appropriate query based on the method name.
	 * </p>
	 *
	 * @param i The stock level to compare against. The query will find products
	 *          with a stock level > i.
	 * @return A list of {@link Product}s that are in stock.
	 */
	List<Product> findByStockLevelGreaterThan(int i);

	/**
	 * Finds all products whose price is within the specified range, inclusive.
	 * <p>
	 * This is a derived query method. Spring Data JPA will generate a query
	 * equivalent to {@code WHERE price BETWEEN initial AND fina}.
	 * </p>
	 *
	 * @param initial The starting price of the range.
	 * @param fina    The ending price of the range.
	 * @return A list of {@link Product}s that fall within the price range.
	 */
	List<Product> findByPriceBetween(int initial, int fina);

	@Query("SELECT new com.cts.model.OverAllStock(p.productID, p.name, p.stockLevel) FROM Product p")
	List<OverAllStock> getAllStocks();
	
	
	@Query("SELECT new com.cts.model.OverAllStock(p.productID, p.name, p.stockLevel) FROM Product p")
	List<ProductDTO> getAllProductQuantity();

	Product findByProductID(int productID);
}
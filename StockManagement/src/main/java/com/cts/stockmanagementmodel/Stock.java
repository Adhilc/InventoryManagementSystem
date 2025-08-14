package com.cts.stockmanagementmodel;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="Stock")
public class Stock {
	@Id
	private int productID;
	private String name;
	private int quantity;
	private int reorderLevel;
	public int getProductID() {
		return productID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setProductID(int productID) {
		this.productID = productID;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getReorderLevel() {
		return reorderLevel;
	}
	public void setReorderLevel(int reorderLevel) {
		this.reorderLevel = reorderLevel;
	}
	public Stock(int productID, String name , int quantity, int reorderLevel) {
		super();
		this.productID = productID;
		this.name= name;
		this.quantity = quantity;
		this.reorderLevel = reorderLevel;
	}
	public Stock() {
		super();
	}
	
	
	
	

	
	

}

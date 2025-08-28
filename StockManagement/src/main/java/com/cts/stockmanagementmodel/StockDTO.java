package com.cts.stockmanagementmodel;

public class StockDTO {
	private int productID;
	private String name;
	private int quantity;
	public int getProductID() {
		return productID;
	}
	public void setProductID(int productID) {
		this.productID = productID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public StockDTO(int productID,String name, int quantity) {
		super();
		this.productID=productID;
		this.name = name;
		this.quantity = quantity;
	}
	public StockDTO() {
		super();
	}
}

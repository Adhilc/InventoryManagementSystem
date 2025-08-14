package com.cts.stockmanagementmodel;

public class QuantityDTO {
	
	private int productID;
	private int quantity;

	public int getProductID() {
		return productID;
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
	public QuantityDTO(int productID, int quantity) {
		super();
		this.productID = productID;
		this.quantity = quantity;
	}
	public QuantityDTO() {
		super();
	}
	

}

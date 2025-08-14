package com.cts.stockmanagementmodel;

public class ProductDTO {
	
	private int productID;
	private int quantity;
	public int getProductID() {
		return productID;
	}
	public void setProductID(int productId) {
		this.productID = productId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public ProductDTO(int productId, int quantity) {
		super();
		this.productID = productId;
		this.quantity = quantity;
	}
	public ProductDTO() {
		super();
	}
	
	

}

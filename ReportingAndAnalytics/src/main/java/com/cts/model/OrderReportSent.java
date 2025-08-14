package com.cts.model;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class OrderReportSent {
	private int productId;
	private LocalDateTime date;
	private int quantity;
	public OrderReportSent(int productId, LocalDateTime date, int quantity) {
        this.productId = productId;
        this.date = date;
        this.quantity = quantity;
    }

}

package com.cts.model;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class OrderReportSent {
	private int productId;
	private LocalDate date;
	private int quantity;
	public OrderReportSent(int productId, LocalDate date, int quantity) {
        this.productId = productId;
        this.date = date;
        this.quantity = quantity;
    }

}

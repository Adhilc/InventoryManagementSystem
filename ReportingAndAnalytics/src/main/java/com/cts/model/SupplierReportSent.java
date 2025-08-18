package com.cts.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierReportSent {
	private String name;
	private String productsSupplied;
	private LocalDateTime date;
	

}

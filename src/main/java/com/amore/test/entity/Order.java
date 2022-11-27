package com.amore.test.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Order {
	private String orderNumber;
	
	private String orderCode;
	
	private String stepCode;
	
	private String orderDate;
	
	private String sendDate;
}

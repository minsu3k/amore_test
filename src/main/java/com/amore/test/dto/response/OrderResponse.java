package com.amore.test.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Builder
public class OrderResponse {
	private String error;
	
	private String message;
	
	private String orderNumber;
	
	private String orderCode;
	
	private String stepCode;
	
	private String orderDate;
	
	private String sendDate;

}

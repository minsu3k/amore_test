package com.amore.test.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class StockResponse {
	
	private String type;
	
	private int quantity;
	
	private String error;
	
	private String message;

}

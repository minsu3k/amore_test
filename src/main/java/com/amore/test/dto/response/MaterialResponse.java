package com.amore.test.dto.response;

import com.amore.test.entity.Material;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Builder
public class MaterialResponse {
	private String error;
	
	private String message;
	
	private String type;
	
	private int quantity;
}

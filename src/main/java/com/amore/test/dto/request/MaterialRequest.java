package com.amore.test.dto.request;

import com.amore.test.dto.response.MaterialResponse;
import com.amore.test.entity.Material;
import com.amore.test.exception.MaterialError;
import com.amore.test.util.CommonUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MaterialRequest {

	private String type;
	
	private int quantity;
	
	public Material toMaterial() {
		return Material.builder().type(type).quantity(quantity).build();
	}

	public MaterialResponse validate() {
		MaterialResponse materialResponse = MaterialResponse.builder().build();
		if (CommonUtil.isNullOrEmpty(type, String.valueOf(quantity))) {
			materialResponse = MaterialResponse.builder()
					 .error(MaterialError.NOT_EXIST_TYPE_OR_QUANTITY.getMessage())
					 .build();
		} else if(quantity > 200) {
			materialResponse = MaterialResponse.builder()
					 .error(MaterialError.INVALID_ADD_QUANTITY.getMessage())
					 .build();
		} else if(quantity < 0) {
			materialResponse = MaterialResponse.builder()
					 .error(MaterialError.INVALID_QUANTITY_VALUE.getMessage())
					 .build();
		}
		return materialResponse;
	}
}

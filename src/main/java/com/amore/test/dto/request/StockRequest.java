package com.amore.test.dto.request;

import com.amore.test.dto.response.StockResponse;
import com.amore.test.entity.Stock;
import com.amore.test.exception.StockError;
import com.amore.test.util.CommonUtil;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StockRequest {
	
	private String type;
	
	private int quantity;
	
	public Stock toStock() {
		return Stock.builder().type(type).quantity(quantity).build();
	}
	
	public StockResponse validate() {
		StockResponse stockResponse = StockResponse.builder().build();
		if (CommonUtil.isNullOrEmpty(type, String.valueOf(quantity))) {
			//throw new CommonException(CommonError.BAD_REQUEST, "필수값 누락");
			stockResponse = StockResponse.builder()
					 .error(StockError.NOT_EXIST_TYPE_OR_QUANTITY.getMessage())
					 .build();
		}else if(quantity < 0) {
			//throw new CommonException(CommonError.BAD_REQUEST, "재고의 양은 0 이상이어야 합니다.");
			stockResponse = StockResponse.builder()
					 .error(StockError.INVALID_QUANTITY.getMessage())
					 .build();
		}
		return stockResponse;
	}
	
}

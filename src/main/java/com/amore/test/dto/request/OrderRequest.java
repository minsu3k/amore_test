package com.amore.test.dto.request;

import org.springframework.util.ObjectUtils;

import com.amore.test.dto.response.OrderResponse;
import com.amore.test.entity.Order;
import com.amore.test.exception.OrderError;
import com.amore.test.util.CommonUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderRequest extends Order {

	private String efficacyType1;

	private String proportion1;

	private String efficacyType2;

	private String proportion2;

	public OrderResponse validate() {
		OrderResponse orderResponse = OrderResponse.builder().build();
		if (CommonUtil.isNullOrEmpty(efficacyType1, proportion1)) {
			orderResponse = OrderResponse.builder()
					 .error(OrderError.NOT_EXIST_EFFICACY_OR_PROPORTION.getMessage())
					 .build();
		} else if (ObjectUtils.isEmpty(efficacyType2)) {
			if (Integer.parseInt(proportion1) != 10) {
				orderResponse = OrderResponse.builder()
						 .error(OrderError.INVALID_SINGLE_PROPORTION.getMessage())
						 .build();
			}
		} else {
			if (CommonUtil.isNullOrEmpty(proportion2)) {
				orderResponse = OrderResponse.builder()
						 .error(OrderError.NOT_EXIST_EFFICACY_OR_PROPORTION.getMessage())
						 .build();
			}
			if (Integer.parseInt(proportion1) + Integer.parseInt(proportion2) != 10) {
				orderResponse = OrderResponse.builder()
						 .error(OrderError.INVALID_SUM_PROPORTION.getMessage())
						 .build();
			}
		}
		return orderResponse;
	}

}

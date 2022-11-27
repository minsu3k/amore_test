package com.amore.test.util;

import com.amore.test.dto.request.OrderRequest;

/**
 * 주문 관리 유틸
 */
public class OrderUtil {

	// 주문 코드 생성
	public static String makeOrderCode(OrderRequest request) {
		StringBuilder sb = new StringBuilder();
		
		if(CommonUtil.isNullOrEmpty(request.getEfficacyType2())) { //원료가 1개일 경우 A10
			sb.append(request.getEfficacyType1())
			  .append(request.getProportion1());
		}else { //원료가 2개일 경우 A3B7
			if(Integer.parseInt(request.getProportion1()) >= Integer.parseInt(request.getProportion2())) {
				sb.append(request.getEfficacyType1())
				  .append(request.getProportion1())
				  .append(request.getEfficacyType2())
				  .append(request.getProportion2());
			}else {
				sb.append(request.getEfficacyType2())
				  .append(request.getProportion2())
				  .append(request.getEfficacyType1())
				  .append(request.getProportion1());
			}
		}
		return sb.toString();
	}
}

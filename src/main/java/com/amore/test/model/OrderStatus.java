package com.amore.test.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * 제품 상태
 *
 */
@Getter
@AllArgsConstructor
public enum OrderStatus {
	RECEIPT("RECEIPT", "주문접수", true),
	PRODUCING("PRODUCING", "제품생산 중", false), // 이후 주문 취소 불가
	PRODUCE_COMPLETE("PRODUCE_COMPLETE", "제품 생산 완료", false),
	SEND_READY("SEND_READY", "발송 준비 중", false),
	SEND_COMPLETE("SEND_COMPLETE", "발송 완료", false),
	CANCEL("CANCEL", "주문 취소", true),
	UNKNOWN("UNKNOWN", "알수 없음", false);

	private String code;

	private String name;
	
	private Boolean cancellable;
	
	public static String getNamefromCode(String code) {
		for (OrderStatus orderStatus : values()) {
			if (orderStatus.getCode().equalsIgnoreCase(code)) {
				return orderStatus.getName();
			}
		}
		return OrderStatus.UNKNOWN.getName();
	}

}

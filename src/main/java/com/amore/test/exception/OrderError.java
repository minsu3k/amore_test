package com.amore.test.exception;

public enum OrderError implements ErrorInterface {
	//주문 접수 에러
	NOT_EXIST_EFFICACY_OR_PROPORTION("500000", "효능 또는 비율을 입력하지 않았습니다."),
	INVALID_SINGLE_PROPORTION("500001", "단일 효능은 비율이 10이어야 합니다."),
	INVALID_SUM_PROPORTION("500002", "혼합 비율의 합은 10이어야 합니다."),
	
	//주문 취소 에러
	NOT_EXIST_ORDER_NO("500010", "주문 번호를 입력하지 않았습니다."),
	INVALID_ORDER_NO("500011", "해당 주문 번호가 존재하지 않습니다."),
	INVALID_CANCEL_STEP("500012", "생산이 진행되어 주문 취소가 불가합니다"),
	
	DISCONTINUED("500020", "효능이 단종되었습니다."),
	
	DELAYED_CANCEL("500030", "생산 지연으로 인해 주문이 취소되었습니다."),

	UNAUTHORIZED("999999", "알수 없는 주문 오류입니다.");

	private String code;
	private String message;
		
	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	private OrderError(String code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public static OrderError getByCode(String code) {
		for (OrderError orderError : OrderError.values()) {
			if (orderError.getCode().equals(code)) {
				return orderError;
			}
		}
		return OrderError.UNAUTHORIZED;
	}
}

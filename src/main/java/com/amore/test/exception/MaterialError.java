package com.amore.test.exception;

public enum MaterialError implements ErrorInterface {
	//원료 에러
	NOT_EXIST_TYPE_OR_QUANTITY("500000", "재고의 종류 또는 수량을 입력하지 않았습니다."),
	INVALID_ADD_QUANTITY("500002", "원료는 200 이하로 추가할 수 있습니다."),
	INVALID_QUANTITY_VALUE("500002", "원료의 양은 0 이상이어야 합니다."),
	
	UNAUTHORIZED("999999", "알수 없는 주문 오류입니다.");

	private String code;
	private String message;
		
	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	private MaterialError(String code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public static MaterialError getByCode(String code) {
		for (MaterialError orderError : MaterialError.values()) {
			if (orderError.getCode().equals(code)) {
				return orderError;
			}
		}
		return MaterialError.UNAUTHORIZED;
	}
}

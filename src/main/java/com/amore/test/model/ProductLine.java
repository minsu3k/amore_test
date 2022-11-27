package com.amore.test.model;

/**
 * 제품 상태
 *
 */
public enum ProductLine {
	RECEIPT("주문접수", true),
	PRODUCING("제품생산 중", false), // 이후 주문 취소 불가
	PRODUCE_COMPLETE("제품 생산 완료", false),
	SEND_READY("발송 준비 중", false),
	SEND_COMPLETE("발송 완료", false);

	private String status;
	
	private Boolean cancellable;

	private ProductLine(String status, Boolean cancellable) {
		this.status = status;
		this.cancellable = cancellable;
	}
		
}

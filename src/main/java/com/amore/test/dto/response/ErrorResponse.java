package com.amore.test.dto.response;

import com.amore.test.exception.ErrorInterface;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ErrorResponse {
	private String code;
	private String message;

	public ErrorResponse(ErrorInterface error, String message) {
		this.code = error.getCode();
		this.message = message;
	}
}

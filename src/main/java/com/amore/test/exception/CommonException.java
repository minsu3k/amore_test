package com.amore.test.exception;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CommonException extends RuntimeException {
	private ErrorInterface error;

	public CommonException(ErrorInterface error) {
		super(String.format("Error=%s", error.getCode()));
		this.error = error;
	}

	public CommonException(ErrorInterface error, String message) {
		super(message);
		this.error = error;
	}

	public CommonException(ErrorInterface error, Throwable e) {
		super(e);
		this.error = error;
	}

	public CommonException(ErrorInterface error, String message, Throwable e) {
		super(message, e);
		this.error = error;
	}

}

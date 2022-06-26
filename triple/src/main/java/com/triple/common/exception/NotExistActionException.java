package com.triple.common.exception;

/**
 * ADD, MOD, DELETE 액션 외의 경우 예외 처리
 */
public class NotExistActionException extends RuntimeException {

	private static final long serialVersionUID = 6271105237049269150L;

	public NotExistActionException() {
		super();
	}

}

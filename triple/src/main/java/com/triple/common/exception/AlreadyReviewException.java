package com.triple.common.exception;

/**
 * 동일 장소 리뷰 저장 예외 처리
 */
public class AlreadyReviewException extends RuntimeException {

	private static final long serialVersionUID = -8267175060421911034L;

	public AlreadyReviewException() {
		super();
	}

}

package com.triple.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 공통 예외 처리 코드 객체
 */
@Getter
@AllArgsConstructor
public enum CommonErrorCode {

	SYSTEM_ERROR("SYSTEM_ERROR", "시스템 에러입니다."),
	NOT_EXIST_TYPE_ERROR("NOT_EXIST_TYPE_ERROR", "요청 권한이 없습니다."),
	NOT_EXIST_ACTION_ERROR("NOT_EXIST_ACTION_ERROR", "요청 권한이 없습니다."),
	ALREADY_REVIEW_ERROR("ALREADY_REVIEW_ERROR", "작성된 리뷰가 존재합니다.");

	private String code;
	private String message;

}

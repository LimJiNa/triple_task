package com.triple.common.response;

import com.triple.common.constants.CommonErrorCode;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 예외 처리 반환 공통 응답 객체
 */
@Data
@NoArgsConstructor
public class CommonResponseError {

	private String code;
	private String message;

	public CommonResponseError(CommonErrorCode commonErrorCode) {
		this.code = commonErrorCode.getCode();
		this.message = commonErrorCode.getMessage();
	}

	public CommonResponseError(String code, String message) {
		this.code = code;
		this.message = message;
	}

}

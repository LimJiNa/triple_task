package com.triple.mileage.common.response;

import com.triple.mileage.common.constants.CommonErrorCode;

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

}

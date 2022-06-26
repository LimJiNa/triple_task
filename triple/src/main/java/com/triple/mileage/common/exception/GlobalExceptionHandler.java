package com.triple.mileage.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.triple.mileage.common.constants.CommonErrorCode;
import com.triple.mileage.common.response.CommonResponseError;

import lombok.extern.slf4j.Slf4j;

/**
 * 공통 예외 처리 핸들러
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = { Exception.class })
	public ResponseEntity<?> ExceptionHandler(Exception e) {
		pringLog(e);

		return new ResponseEntity<>(new CommonResponseError(CommonErrorCode.SYSTEM_ERROR),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(value = { NotExistTypeException.class })
	public ResponseEntity<?> NotExistTypeExceptionHandler(NotExistTypeException e) {
		pringLog(e);

		return new ResponseEntity<>(new CommonResponseError(CommonErrorCode.NOT_EXIST_TYPE_ERROR),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = { NotExistActionException.class })
	public ResponseEntity<?> NotExistActionExceptionHandler(NotExistActionException e) {
		pringLog(e);

		return new ResponseEntity<>(new CommonResponseError(CommonErrorCode.NOT_EXIST_ACTION_ERROR),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = { AlreadyReviewException.class })
	public ResponseEntity<?> AlreadyReviewExceptionHandler(AlreadyReviewException e) {
		pringLog(e);

		return new ResponseEntity<>(new CommonResponseError(CommonErrorCode.ALREADY_REVIEW_ERROR),
				HttpStatus.BAD_REQUEST);
	}

	private void pringLog(Exception e) {
		log.error("Exception : {}", e);
	}

}

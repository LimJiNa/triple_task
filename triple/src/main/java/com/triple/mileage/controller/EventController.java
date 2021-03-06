package com.triple.mileage.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.triple.common.constants.CommonConstants;
import com.triple.common.exception.NotExistActionException;
import com.triple.common.response.CommonResponseData;
import com.triple.common.response.CommonResponseList;
import com.triple.mileage.dto.EventDTO;
import com.triple.mileage.service.EventService;

import lombok.extern.slf4j.Slf4j;

/**
 * 리뷰 이벤트 후 전달 받을 포인트 이벤트 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/events")
public class EventController {

	@Autowired
	private EventService eventService;

	/**
	 * (전체) 포인트 부여 히스토리 조회
	 * 
	 * @return Object data
	 */
	@GetMapping
	public ResponseEntity<?> getPointHistory() {
		log.debug("/events");

		CommonResponseList response = new CommonResponseList();
		response.setDataList(eventService.getPointHistory());
		
		return ResponseEntity.ok().body(response);
	}

	/**
	 * (개인) 포인트 부여 히스토리 조회
	 * 
	 * @param String userId
	 * @return List<Object> dataList
	 */
	@GetMapping("/{userId}")
	public ResponseEntity<?> getPointHistoryByUser(@PathVariable String userId) {
		log.debug("/events/{}", userId);

		CommonResponseList response = new CommonResponseList();
		response.setDataList(eventService.getPointHistoryByUser(userId));
		
		return ResponseEntity.ok().body(response);
	}

	/**
	 * 사용자별 누적 포인트 조회
	 * 
	 * @param String userId
	 * @return Object data
	 */
	@GetMapping("/{userId}/points")
	public ResponseEntity<?> getPointByUser(@PathVariable String userId) {
		log.debug("/events/{}/points", userId);

		CommonResponseData response = new CommonResponseData();
		response.setData(eventService.getPointByUser(userId));

		return ResponseEntity.ok().body(response);
	}

	/**
	 * 리뷰 이벤트로 인한 포인트 적립 히스토리의 저장, 수정, 삭제 &&
	 * 사용자 누적 포인트 증차감 저장, 삭제
	 * 
	 * @param EventDTO eventDto
	 * @return Object Data
	 */
	@PostMapping
	public ResponseEntity<?> savePoint(@RequestBody @Valid EventDTO eventDto) {
		log.debug("/events param : {}", eventDto);

		CommonResponseData response = new CommonResponseData();

		if (eventDto.getAction().equals(CommonConstants.ACTION_ADD)) {
			response.setData(eventService.savePoint(eventDto));
		} else if (eventDto.getAction().equals(CommonConstants.ACTION_MOD)) {
			response.setData(eventService.updatePoint(eventDto));
		} else if (eventDto.getAction().equals(CommonConstants.ACTION_DELETE)){
			response.setData(eventService.deletePoint(eventDto));
		} else {
			/**
			 * ADD, MOD, DELETE 액션 외의 경우 예외 처리
			 */
			throw new NotExistActionException();
		}

		return ResponseEntity.ok().body(response);
	}

}

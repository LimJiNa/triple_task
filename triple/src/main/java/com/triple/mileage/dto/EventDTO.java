package com.triple.mileage.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 리뷰 이벤트 후 전달 받을 포인트 이벤트 객체
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EventDTO {

	private String type;
	private String action;
	private String reviewId;
	private String content;
	private List<String> attachedPhotoIds;
	private String userId;
	private String placeId;

}

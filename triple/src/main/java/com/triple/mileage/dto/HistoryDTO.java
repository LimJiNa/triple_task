package com.triple.mileage.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 포인트 부여 히스토리 반환 객체
 */
@NoArgsConstructor
@Data
public class HistoryDTO {

	private String action;
	private String reviewId;
	private String userId;
	private String placeId;
	private String saveDateTime;
	private long point;

	@QueryProjection
	public HistoryDTO(String action, String reviewId, String userId, String placeId, LocalDateTime saveDateTime, long point) {
		this.action = action;
		this.reviewId = reviewId;
		this.userId = userId;
		this.placeId = placeId;
		this.saveDateTime = saveDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		this.point = point;
	}

}

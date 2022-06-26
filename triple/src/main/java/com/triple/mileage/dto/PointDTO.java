package com.triple.mileage.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 사용자 누적 포인트 반환 객체
 */
@NoArgsConstructor
@Data
public class PointDTO {

	private String userId;
	private long accuePoint;

	@QueryProjection
	public PointDTO(String userId, long accuePoint) {
		this.userId = userId;
		this.accuePoint = accuePoint;
	}

}

package com.triple.mileage.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import com.triple.mileage.dto.EventDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 사용자 누적 포인트 테이블 객체
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(indexes = { @Index(columnList = "userId") })
public class Point {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 20)
	private long pointId;

	@Column(nullable = false, length = 40)
	private String userId;

	@Column(nullable = false, length = 20)
	private long accuePoint;

	public static Point dtoToPoint(final EventDTO eventDto) {
		return Point.builder()
				.userId(eventDto.getUserId())
				.build();
	}

}

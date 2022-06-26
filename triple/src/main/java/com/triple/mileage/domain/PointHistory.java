package com.triple.mileage.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.triple.mileage.dto.EventDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 포인트 부여 히스토리 테이블 객체
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(indexes = { @Index(columnList = "reviewId, userId, placeId") })
public class PointHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 20)
	private long pointHistoryId;
	
	@Column(nullable = false, length = 10)
	private String action;
	
	@Column(nullable = false, length = 40)
	private String reviewId;
	
	@Column(nullable = false, length = 40)
	private String userId;
	
	@Column(nullable = false, length = 40)
	private String placeId;
	
	@Column(nullable = false)
	private LocalDateTime saveDateTime;
	
	@Column(nullable = false, length = 20)
	private long point;
	
	@Column(nullable = false, length = 11)
	private int attachedCount;
	
	@Column(nullable = false, length = 1)
	private char deleteYn;

	@PrePersist
	public void saveDateTime() {
		this.saveDateTime = LocalDateTime.now();
	}

	public static PointHistory dtoToPointHistory(EventDTO eventDto) {
		return PointHistory.builder()
				.action(eventDto.getAction())
				.reviewId(eventDto.getReviewId())
				.userId(eventDto.getUserId())
				.placeId(eventDto.getPlaceId())
				.attachedCount(eventDto.getAttachedPhotoIds().size())
				.build();
	}

}

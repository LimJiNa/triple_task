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
@Table(name = "pointHistory", indexes = { @Index(columnList = "reviewId, userId, placeId") })
public class History {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long pointHistoryId;
	
	@Column(nullable = false)
	private String action;
	
	@Column(nullable = false)
	private String reviewId;
	
	@Column(nullable = false)
	private String userId;
	
	@Column(nullable = false)
	private String placeId;
	
	@Column(nullable = true)
	private LocalDateTime saveDateTime;
	
	@Column(nullable = false)
	private long point;
	
	@Column(nullable = true)
	private int attachedCount;
	
	@Column(nullable = true)
	private char deleteYn;

	@PrePersist
	public void saveDateTime() {
		this.saveDateTime = LocalDateTime.now();
	}

	public static History dtoToPointHistory(EventDTO eventDto) {
		return History.builder()
				.action(eventDto.getAction())
				.reviewId(eventDto.getReviewId())
				.userId(eventDto.getUserId())
				.placeId(eventDto.getPlaceId())
				.attachedCount(eventDto.getAttachedPhotoIds().size())
				.build();
	}

}

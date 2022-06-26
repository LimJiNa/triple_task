package com.triple.mileage.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

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

	@NotNull
	private String type;
	
	@NotNull
	@Length(max = 10)
	private String action;
	
	@NotNull
	@Length(max = 40)
	private String reviewId;
	
	@NotNull
	private String content;
	
	private List<String> attachedPhotoIds;
	
	@NotNull
	@Length(max = 40)
	private String userId;
	
	@NotNull
	@Length(max = 40)
	private String placeId;

}

package com.triple.mileage.service;

import java.util.List;

import com.triple.mileage.dto.EventDTO;
import com.triple.mileage.dto.HistoryDTO;
import com.triple.mileage.dto.PointDTO;

/**
 * 리뷰 이벤트 후 전달 받을 포인트 이벤트 서비스
 */
public interface EventService {

	/**
	 * (전체) 포인트 부여 히스토리 조회
	 * 
	 * @return List<HistoryDTO>
	 */
	public List<HistoryDTO> getHistory();

	/**
	 * (개인) 포인트 부여 히스토리 조회
	 * 
	 * @param String userId
	 * @return List<HistoryDTO>
	 */
	public List<HistoryDTO> getHistoryByUser(String userId);

	/**
	 * 사용자 누적 포인트 조회
	 * 
	 * @param String userId
	 * @return PointDTO
	 */
	public PointDTO getPointByUser(String userId);

	/**
	 * 리뷰 저장으로 인한 포인트 적립 히스토리 저장 및 사용자 누적 포인트 증가
	 * 
	 * @param EventDTO eventDto
	 * @return PointDTO
	 */
	public PointDTO savePoint(EventDTO eventDto);

	/**
	 * 리뷰 수정으로 인한 포인트 증감 히스토리 저장 및 사용자 누적 포인트 증차감
	 * 
	 * @param EventDTO eventDto
	 * @return PointDTO
	 */
	public PointDTO updatePoint(EventDTO eventDto);

	/**
	 * 리뷰 삭제로 인한 포인트 차감 히스토리 저장 및 사용자 누적 포인트 차감
	 * 
	 * @param EventDTO eventDto
	 * @return PointDTO
	 */
	public PointDTO deletePoint(EventDTO eventDto);

}

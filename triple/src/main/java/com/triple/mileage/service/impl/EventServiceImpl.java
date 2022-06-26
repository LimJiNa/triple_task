package com.triple.mileage.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.triple.common.constants.CommonConstants;
import com.triple.common.exception.AlreadyReviewException;
import com.triple.common.exception.NotExistTypeException;
import com.triple.mileage.domain.PointHistory;
import com.triple.mileage.domain.Point;
import com.triple.mileage.dto.EventDTO;
import com.triple.mileage.dto.PointHistoryDTO;
import com.triple.mileage.dto.PointDTO;
import com.triple.mileage.repository.PointHistoryRepository;
import com.triple.mileage.repository.PointRepository;
import com.triple.mileage.service.EventService;

/**
 * 리뷰 이벤트 후 전달 받을 포인트 이벤트 서비스
 */
@Service
public class EventServiceImpl implements EventService {

	@Autowired
	private PointHistoryRepository pointHistoryRepository;

	@Autowired
	private PointRepository pointRepository;

	/**
	 * (전체) 포인트 부여 히스토리 조회
	 * 
	 * @return List<PointHistoryDTO>
	 */
	@Override
	public List<PointHistoryDTO> getPointHistory() {
		return pointHistoryRepository.getPointHistory();
	}

	/**
	 * (개인) 포인트 부여 히스토리 조회
	 * 
	 * @param String userId
	 * @return List<PointHistoryDTO>
	 */
	@Override
	public List<PointHistoryDTO> getPointHistoryByUser(String userId) {
		return pointHistoryRepository.getPointHistoryByUser(userId);
	}

	/**
	 * 사용자 누적 포인트 조회
	 * 
	 * @param String userId
	 * @return PointDTO
	 */
	@Override
	public PointDTO getPointByUser(String userId) {
		return pointRepository.getPointByUser(userId);
	}

	/**
	 * 리뷰 저장으로 인한 포인트 적립 히스토리 저장 및 사용자 누적 포인트 증가
	 * 
	 * @param EventDTO eventDto
	 * @return PointDTO
	 */
	@Transactional
	@Override
	public PointDTO savePoint(EventDTO eventDto) {
		isValid(eventDto);

		long savePoint = getSavePoint(eventDto);

		PointHistory pointHistory = PointHistory.dtoToPointHistory(eventDto);
		pointHistory.setPoint(savePoint);
		pointHistory.setDeleteYn(CommonConstants.DELETE_N);
		pointHistoryRepository.save(pointHistory);

		Optional<Long> userAccuePoint = pointRepository.getAccuePointByUser(eventDto.getUserId());

		if (userAccuePoint.isPresent()) {
			Long accuePoint = userAccuePoint.get();
			pointRepository.update(accuePoint + savePoint, eventDto.getUserId());
		} else {
			Point point = Point.dtoToPoint(eventDto);
			point.setAccuePoint(savePoint);
			pointRepository.save(point);
		}

		return getPointByUser(eventDto.getUserId());
	}

	/**
	 * 리뷰 수정으로 인한 포인트 증감 히스토리 저장 및 사용자 누적 포인트 증차감
	 * 
	 * @param EventDTO eventDto
	 * @return PointDTO
	 */
	@Transactional
	@Override
	public PointDTO updatePoint(EventDTO eventDto) {
		isValid(eventDto);

		long accuePoint = 0;
		long updatePoint = getUpdatePoint(eventDto);

		PointHistory pointHistory = PointHistory.dtoToPointHistory(eventDto);
		pointHistory.setPoint(updatePoint);
		pointHistoryRepository.save(pointHistory);

		Optional<Long> userAccuePoint = pointRepository.getAccuePointByUser(eventDto.getUserId());

		if (userAccuePoint.isPresent()) {
			accuePoint = userAccuePoint.get();
		}

		pointRepository.update(accuePoint + updatePoint, eventDto.getUserId());

		return getPointByUser(eventDto.getUserId());
	}

	/**
	 * 리뷰 삭제로 인한 포인트 차감 히스토리 저장 및 사용자 누적 포인트 차감
	 * 
	 * @param EventDTO eventDto
	 * @return PointDTO
	 */
	@Transactional
	@Override
	public PointDTO deletePoint(EventDTO eventDto) {
		isValid(eventDto);

		long deletePoint = 0;

		Optional<Long> reviewTotalPoint = pointHistoryRepository.getReviewTotalPoint(eventDto.getReviewId());

		if (reviewTotalPoint.isPresent()) {
			deletePoint = (reviewTotalPoint.get() * -1);
		}

		PointHistory pointHistory = PointHistory.dtoToPointHistory(eventDto);
		pointHistory.setPoint(deletePoint);
		pointHistoryRepository.save(pointHistory);
		pointHistoryRepository.updateDeleteYn(eventDto.getReviewId());

		long accuePoint = 0;

		Optional<Long> userAccuePoint = pointRepository.getAccuePointByUser(eventDto.getUserId());

		if (userAccuePoint.isPresent()) {
			accuePoint = userAccuePoint.get();
		}

		pointRepository.update(accuePoint + deletePoint, eventDto.getUserId());

		return getPointByUser(eventDto.getUserId());
	}

	/**
	 * 포인트 적립시 취득 점수 계산
	 * 
	 * @param EventDTO eventDto
	 * @return long savePoint
	 */
	private long getSavePoint(EventDTO eventDto) {
		long savePoint = 0;

		/**
		 * 텍스트가 1자 이상일 경우 점수 취득
		 */
		if (!StringUtils.isBlank(eventDto.getContent())) {
			savePoint += CommonConstants.CONTENT_POINT;
		}

		/**
		 * 첨부된 사진이 1장 이상일 경우 점수 취득
		 */
		if (!eventDto.getAttachedPhotoIds().isEmpty()) {
			savePoint += CommonConstants.ATTACHED_POINT;
		}

		/**
		 * 해당 장소의 첫 리뷰일 경우 점수 취득
		 */
		if (pointHistoryRepository.firstReviewChecking(eventDto.getPlaceId()).isEmpty()) {
			savePoint += CommonConstants.BONUS_POINT;
		}

		return savePoint;
	}

	/**
	 * 포인트 수정시 증차감 점수 계산
	 * 
	 * @param EventDTO eventDto
	 * @return long updatePoint
	 */
	private long getUpdatePoint(EventDTO eventDto) {
		long updatePoint = 0;

		/**
		 * 사진을 모두 삭제한 경우 점수 차감
		 */
		if (eventDto.getAttachedPhotoIds().size() == 0) {
			updatePoint += CommonConstants.MINU_ATTACHED_POINT;
		}

		/**
		 * 텍스트만 존재하는 리뷰에 사진만 추가되는 경우 점수 취득
		 */
		if (!StringUtils.isBlank(eventDto.getContent()) && !eventDto.getAttachedPhotoIds().isEmpty()) {
			if (pointHistoryRepository.getReviewAttachedCount(eventDto.getReviewId()).get() == 0) {
				updatePoint += CommonConstants.ATTACHED_POINT;
			}
		}

		return updatePoint;
	}

	/**
	 * 예외처리
	 * 
	 * @param EventDTO eventDto
	 */
	private void isValid(final EventDTO eventDto) {
		/**
		 * REVIEW 타입 외의 경우 예외 처리
		 */
		if (!eventDto.getType().equals(CommonConstants.TYPE)) {
			throw new NotExistTypeException();
		}

		/**
		 * 동일 장소 리뷰 저장 예외 처리
		 */
		if (eventDto.getAction().equals(CommonConstants.ACTION_ADD)) {
			if (pointHistoryRepository.alreadyReviewChecking(eventDto.getUserId(), eventDto.getPlaceId()).isPresent()) {
				throw new AlreadyReviewException();
			}
		}
	}

}

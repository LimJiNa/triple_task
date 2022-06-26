package com.triple.mileage.repository;

import static com.triple.mileage.domain.QPointHistory.pointHistory;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.triple.common.constants.CommonConstants;
import com.triple.mileage.domain.PointHistory;
import com.triple.mileage.dto.PointHistoryDTO;
import com.triple.mileage.dto.QPointHistoryDTO;

import lombok.RequiredArgsConstructor;;

/**
 * 포인트 부여 히스토리 관련 레포지토리
 */
@Repository
@RequiredArgsConstructor
public class PointHistoryRepository {

	private final EntityManager entityManager;
	
	private final JPAQueryFactory queryFactory;
	
	/**
	 * (전체) 포인트 부여 히스토리 조회
	 * 
	 * @return List<PointHistoryDTO>
	 */
	public List<PointHistoryDTO> getPointHistory() {
		return queryFactory
				.select(new QPointHistoryDTO(
					pointHistory.action,
					pointHistory.reviewId,
					pointHistory.userId,
					pointHistory.placeId,
					pointHistory.saveDateTime,
					pointHistory.point
				))
				.from(pointHistory)
				.orderBy(pointHistory.saveDateTime.desc())
				.fetch();
	}
	
	/**
	 * (개인) 포인트 부여 히스토리 조회
	 * 
	 * @param String userId
	 * @return List<PointHistoryDTO>
	 */
	public List<PointHistoryDTO> getPointHistoryByUser(String userId) {
		return queryFactory
				.select(new QPointHistoryDTO(
					pointHistory.action,
					pointHistory.reviewId,
					pointHistory.userId,
					pointHistory.placeId,
					pointHistory.saveDateTime,
					pointHistory.point
				))
				.from(pointHistory)
				.where(
					pointHistory.userId.eq(userId)
				)
				.orderBy(pointHistory.saveDateTime.desc())
				.fetch();
	}
	
	/**
	 * 포인트 부여 히스토리 저장
	 * 
	 * @param PointHistory pointHistory
	 */
	public void save(PointHistory pointHistory) {
		entityManager.persist(pointHistory);
	}
	
	/**
	 * 리뷰 삭제시 삭제 여부 -> 삭제로 수정
	 * 
	 * @param String reviewId
	 */
	public void updateDeleteYn(String reviewId) {
		queryFactory
			.update(pointHistory)
			.set(pointHistory.deleteYn, CommonConstants.DELETE_Y)
			.where(
				pointHistory.reviewId.eq(reviewId)
			)
			.execute();
		
		entityManager.flush();
		entityManager.clear();
	}
	
	/**
	 * 리뷰로 취득한 포인트를 합산
	 * 
	 * @param String reviewId
	 * @return Optional<Long> history.point.sum()
	 */
	public Optional<Long> getReviewTotalPoint(String reviewId) {
		return Optional.ofNullable(queryFactory
				.select(
					pointHistory.point.sum()
				)
				.from(pointHistory)
				.where(
					pointHistory.reviewId.eq(reviewId)
				)
				.groupBy(pointHistory.reviewId)
				.fetchOne());
	}
	
	/**
	 * 최근 변동된 첨부 사진의 갯수 취득
	 * 
	 * @param String reviewId
	 * @return Optional<Integer> attachedCount
	 */
	public Optional<Integer> getReviewAttachedCount(String reviewId) {
		return Optional.ofNullable(queryFactory
				.select(
					pointHistory.attachedCount
				)
				.from(pointHistory)
				.where(
					pointHistory.reviewId.eq(reviewId),
					pointHistory.deleteYn.eq(CommonConstants.DELETE_N)
				)
				.orderBy(pointHistory.saveDateTime.desc())
				.fetchFirst());
	}
	
	/**
	 * 리뷰 장소의 첫 리뷰 여부 체크
	 * 
	 * @param String placeId
	 * @return Optional<String> reviewId
	 */
	public Optional<String> firstReviewChecking(String placeId) {
		return Optional.ofNullable(queryFactory
				.select(
					pointHistory.reviewId
				)
				.from(pointHistory)
				.where(
					pointHistory.placeId.eq(placeId),
					pointHistory.deleteYn.eq(CommonConstants.DELETE_N)
				)
				.fetchFirst());
	}
	
	/**
	 * 동일 장소 리뷰 저장 체크(한 사용자 == 한 장소)
	 * 
	 * @param String userId
	 * @param String placeId
	 * @return Optional<String> placeId
	 */
	public Optional<String> alreadyReviewChecking(String userId, String placeId) {
		return Optional.ofNullable(queryFactory
				.select(
					pointHistory.placeId
				)
				.from(pointHistory)
				.where(
					pointHistory.userId.eq(userId),
					pointHistory.placeId.eq(placeId),
					pointHistory.deleteYn.eq(CommonConstants.DELETE_N)
				)
				.fetchFirst());
	}
}

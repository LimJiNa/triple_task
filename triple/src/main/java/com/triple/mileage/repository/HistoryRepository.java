package com.triple.mileage.repository;

import static com.triple.mileage.domain.QHistory.history;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.triple.mileage.common.constants.CommonConstants;
import com.triple.mileage.domain.History;
import com.triple.mileage.dto.HistoryDTO;
import com.triple.mileage.dto.QHistoryDTO;

import lombok.RequiredArgsConstructor;;

/**
 * 포인트 부여 히스토리 관련 레포지토리
 */
@Repository
@RequiredArgsConstructor
public class HistoryRepository {

	private final EntityManager entityManager;
	
	private final JPAQueryFactory queryFactory;
	
	/**
	 * (전체) 포인트 부여 히스토리 조회
	 * 
	 * @return List<HistoryDTO>
	 */
	public List<HistoryDTO> getHistory() {
		return queryFactory
				.select(new QHistoryDTO(
					history.action,
					history.reviewId,
					history.userId,
					history.placeId,
					history.saveDateTime,
					history.point
				))
				.from(history)
				.orderBy(history.saveDateTime.desc())
				.fetch();
	}
	
	/**
	 * (개인) 포인트 부여 히스토리 조회
	 * 
	 * @param String userId
	 * @return List<HistoryDTO>
	 */
	public List<HistoryDTO> getHistoryByUser(String userId) {
		return queryFactory
				.select(new QHistoryDTO(
					history.action,
					history.reviewId,
					history.userId,
					history.placeId,
					history.saveDateTime,
					history.point
				))
				.from(history)
				.where(
					history.userId.eq(userId)
				)
				.orderBy(history.saveDateTime.desc())
				.fetch();
	}
	
	/**
	 * 포인트 부여 히스토리 저장
	 * 
	 * @param History history
	 */
	public void save(History history) {
		entityManager.persist(history);
	}
	
	/**
	 * 리뷰 삭제시 삭제 여부 -> 삭제로 수정
	 * 
	 * @param String reviewId
	 */
	public void updateDeleteYn(String reviewId) {
		queryFactory
			.update(history)
			.set(history.deleteYn, CommonConstants.DELETE_Y)
			.where(
				history.reviewId.eq(reviewId)
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
					history.point.sum()
				)
				.from(history)
				.where(
					history.reviewId.eq(reviewId)
				)
				.groupBy(history.reviewId)
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
					history.attachedCount
				)
				.from(history)
				.where(
					history.reviewId.eq(reviewId),
					history.deleteYn.eq(CommonConstants.DELETE_N)
				)
				.orderBy(history.saveDateTime.desc())
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
					history.reviewId
				)
				.from(history)
				.where(
					history.placeId.eq(placeId),
					history.deleteYn.eq(CommonConstants.DELETE_N)
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
					history.placeId
				)
				.from(history)
				.where(
					history.userId.eq(userId),
					history.placeId.eq(placeId),
					history.deleteYn.eq(CommonConstants.DELETE_N)
				)
				.fetchFirst());
	}
}

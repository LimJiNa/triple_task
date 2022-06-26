package com.triple.mileage.repository;

import static com.triple.mileage.domain.QPoint.point;

import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.triple.mileage.domain.Point;
import com.triple.mileage.dto.PointDTO;
import com.triple.mileage.dto.QPointDTO;

import lombok.RequiredArgsConstructor;;

/**
 * 사용자 누적 포인트 관련 레포지토리
 */
@Repository
@RequiredArgsConstructor
public class PointRepository {

	private final EntityManager entityManager;
	
	private final JPAQueryFactory queryFactory;
	
	/**
	 * 사용자별 누적 포인트 조회
	 * 
	 * @param String userId
	 * @return PointDTO
	 */
	public PointDTO getPointByUser(String userId) {
		return queryFactory
				.select(new QPointDTO(
					point.userId,
					point.accuePoint
				))
				.from(point)
				.where(
					point.userId.eq(userId)	
				)
				.fetchOne();
	}
	
	/**
	 * 사용자 누적 포인트 저장
	 * 
	 * @param Point point
	 */
	public void save(Point point) {
		entityManager.persist(point);
	}

	/**
	 * 사용자 누적 포인트 수정
	 * 
	 * @param String accuePoint
	 * @param String userId
	 */
	public void update(Long accuePoint, String userId) {
		queryFactory
			.update(point)
			.set(point.accuePoint, accuePoint)
			.where(
				point.userId.eq(userId)
			)
			.execute();
			
			entityManager.flush();
			entityManager.clear();
	}
	
	/**
	 * 사용자의 누적 포인트 점수 취득
	 * 
	 * @param String userId
	 * @return Optional<Long> accuePoint
	 */
	public Optional<Long> getAccuePointByUser(String userId) {
		return Optional.ofNullable(queryFactory
				.select(
					point.accuePoint
				)
				.from(point)
				.where(
					point.userId.eq(userId)	
				)
				.fetchOne());
	}
	
}

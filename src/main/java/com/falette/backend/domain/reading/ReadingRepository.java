package com.falette.backend.domain.reading;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ReadingRepository extends JpaRepository<Reading, Long> {

    /** 캘린더용: 특정 사용자의 [from, to) 기간 리딩을 최신순으로 */
    @Query("""
            select r from Reading r
            where r.user.id = :userId
              and r.createdAt >= :from and r.createdAt < :to
            order by r.createdAt desc
            """)
    List<Reading> findByUserInPeriod(@Param("userId") Long userId,
                                     @Param("from") Instant from,
                                     @Param("to") Instant to);

    /** 상세 조회용: 카드까지 한 번에 가져오기 */
    @Query("select r from Reading r join fetch r.tarotCard join fetch r.user where r.id = :id")
    Optional<Reading> findWithCardById(@Param("id") Long id);
}

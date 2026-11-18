package com.falette.backend.domain.reading;

import com.falette.backend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    /** 비회원 → 구글 계정 합치기: from 사용자의 리딩을 전부 to 사용자로 옮기고, 옮긴 개수를 반환 */
    @Modifying(clearAutomatically = true)
    @Query("update Reading r set r.user = :to where r.user = :from")
    int reassignUser(@Param("from") User from, @Param("to") User to);

    /** 상세 조회용: 카드까지 한 번에 가져오기 */
    @Query("select r from Reading r join fetch r.tarotCard join fetch r.user where r.id = :id")
    Optional<Reading> findWithCardById(@Param("id") Long id);
}

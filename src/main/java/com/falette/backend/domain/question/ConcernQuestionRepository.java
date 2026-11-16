package com.falette.backend.domain.question;

import com.falette.backend.domain.reading.ConcernType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConcernQuestionRepository extends JpaRepository<ConcernQuestion, Long> {

    /** 1차 질문 조회: questionOrder = 1 */
    Optional<ConcernQuestion> findByConcernTypeAndQuestionOrder(ConcernType concernType, int questionOrder);

    /** 해당 고민유형의 2차 질문 전체 (1차 선택지별로 하나씩) */
    List<ConcernQuestion> findAllByConcernTypeAndQuestionOrder(ConcernType concernType, int questionOrder);

    /** 1차 선택지 id에 딸린 2차 질문 (POST 검증용) */
    Optional<ConcernQuestion> findByParentOptionId(Long parentOptionId);
}
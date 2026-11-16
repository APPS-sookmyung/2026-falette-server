package com.falette.backend.domain.reading;

import com.falette.backend.common.entity.BaseTimeEntity;
import com.falette.backend.domain.card.TarotCard;
import com.falette.backend.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

/**
 * READING 테이블 (DB 데이터 설계 5번 기준).
 * - 저장(POST /api/v1/tarot/readings)은 리딩 플로우 담당, 조회(캘린더/상세)는 마이페이지 담당이 사용하는 공용 엔티티.
 * - 생성 로직은 리딩 플로우 담당이 추가할 수 있게 비워 둠.
 */
@Getter
@Entity
@Table(name = "reading", indexes = @Index(name = "idx_reading_user_created", columnList = "user_id, created_at"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reading extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "concern_type", nullable = false, length = 30)
    private ConcernType concernType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ColorOption color;

    @Enumerated(EnumType.STRING)
    @Column(name = "brightness_level", nullable = false, length = 20)
    private ColorLevel brightnessLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "saturation_level", nullable = false, length = 20)
    private ColorLevel saturationLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "color_reason", nullable = false, length = 100)
    private ColorReason colorReason;

    /**
     * CONCERN_QUESTION_OPTION.id 참조. 질문/선택지 엔티티는 리딩 플로우 담당 쪽이라
     * 우선 ID 값만 저장하고, ConcernQuestionOption 엔티티가 생기면 @ManyToOne으로 바꾸면 됨.
     */
    @Column(name = "concern_option1_id")
    private Long concernOption1Id;

    @Column(name = "concern_option2_id")
    private Long concernOption2Id;

    @Column(name = "question1_answer", columnDefinition = "TEXT")
    private String question1Answer;

    @Column(name = "question2_answer", columnDefinition = "TEXT")
    private String question2Answer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tarot_card_id", nullable = false)
    private TarotCard tarotCard;

    @Column(name = "ai_summary", nullable = false)
    private String aiSummary;

    @Column(name = "ai_interpretation", nullable = false, columnDefinition = "TEXT")
    private String aiInterpretation;

    /** 쉼표로 구분해 저장 (예: "새로운 시작,순수함,직관적 선택") */
    @Column(name = "ai_keywords")
    private String aiKeywords;

    @Column(name = "lucky_item", length = 100)
    private String luckyItem;

    public List<String> getAiKeywordList() {
        if (aiKeywords == null || aiKeywords.isBlank()) {
            return List.of();
        }
        return Arrays.stream(aiKeywords.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    public boolean isOwnedBy(User user) {
        return this.user.getId().equals(user.getId());
    }

    public static Reading create(User user,
                                 ConcernType concernType,
                                 ColorOption color,
                                 ColorLevel brightnessLevel,
                                 ColorLevel saturationLevel,
                                 ColorReason colorReason,
                                 Long concernOption1Id,
                                 Long concernOption2Id,
                                 String question1Answer,
                                 String question2Answer,
                                 TarotCard tarotCard,
                                 String aiSummary,
                                 String aiInterpretation,
                                 List<String> aiKeywords,
                                 String luckyItem) {
        Reading r = new Reading();
        r.user = user;
        r.concernType = concernType;
        r.color = color;
        r.brightnessLevel = brightnessLevel;
        r.saturationLevel = saturationLevel;
        r.colorReason = colorReason;
        r.concernOption1Id = concernOption1Id;
        r.concernOption2Id = concernOption2Id;
        r.question1Answer = question1Answer;
        r.question2Answer = question2Answer;
        r.tarotCard = tarotCard;
        r.aiSummary = aiSummary;
        r.aiInterpretation = aiInterpretation;
        r.aiKeywords = String.join(",", aiKeywords);
        r.luckyItem = luckyItem;
        return r;
    }
}

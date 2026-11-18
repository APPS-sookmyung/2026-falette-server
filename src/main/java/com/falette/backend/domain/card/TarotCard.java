package com.falette.backend.domain.card;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * TAROT_CARD 마스터 데이터 (20장). Reading이 FK로 참조하기 때문에 엔티티만 정의.
 * 시드 데이터 / 카드 목록 API는 리딩 플로우 담당 쪽 작업.
 */
@Getter
@Entity
@Table(name = "tarot_card")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TarotCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "eng_name", nullable = false, length = 100)
    private String engName;

    @Column(name = "kor_name", nullable = false, length = 100)
    private String korName;

    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl;

    @Column(name = "positivity_level", nullable = false)
    private Integer positivityLevel;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(length = 255)
    private String keywords;

    public static TarotCard create(String engName, String korName, String imageUrl, int positivityLevel,
                                   String description, String keywords) {
        TarotCard card = new TarotCard();
        card.engName = engName;
        card.korName = korName;
        card.imageUrl = imageUrl;
        card.positivityLevel = positivityLevel;
        card.description = description;
        card.keywords = keywords;
        return card;
    }

    /** 프롬프트·목업 문장용 표기: "The Sun (태양)" */
    public String displayName() {
        return engName + " (" + korName + ")";
    }
}
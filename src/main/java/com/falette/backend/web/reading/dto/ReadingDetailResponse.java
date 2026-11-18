package com.falette.backend.web.reading.dto;

import com.falette.backend.domain.card.TarotCard;
import com.falette.backend.domain.reading.ColorOption;
import com.falette.backend.domain.reading.Reading;

import java.time.Instant;
import java.util.List;

/**
 * 리딩 상세 응답 (API 명세서 3-2).
 * 명세상 POST /api/v1/tarot/readings(3-1) 응답과 형태가 같아서 그쪽에서도 그대로 재사용 가능.
 */
public record ReadingDetailResponse(
        Long readingId,
        CardResponse card,
        ColorOption color,
        String aiSummary,
        String aiInterpretation,
        List<String> aiKeywords,
        String luckyItem,
        Instant createdAt
) {
    public static ReadingDetailResponse from(Reading reading) {
        return new ReadingDetailResponse(
                reading.getId(),
                CardResponse.from(reading.getTarotCard()),
                reading.getColor(),
                reading.getAiSummary(),
                reading.getAiInterpretation(),
                reading.getAiKeywordList(),
                reading.getLuckyItem(),
                reading.getCreatedAt()
        );
    }

    public record CardResponse(Long cardId, String engName, String korName, String imageUrl, String description) {
        public static CardResponse from(TarotCard card) {
            return new CardResponse(card.getId(), card.getEngName(), card.getKorName(), card.getImageUrl(), card.getDescription());
        }
    }
}

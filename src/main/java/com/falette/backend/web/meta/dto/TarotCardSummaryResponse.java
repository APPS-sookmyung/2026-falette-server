package com.falette.backend.web.meta.dto;

import com.falette.backend.domain.card.TarotCard;

public record TarotCardSummaryResponse(Long cardId, String engName, String korName, String imageUrl) {

    public static TarotCardSummaryResponse from(TarotCard card) {
        return new TarotCardSummaryResponse(card.getId(), card.getEngName(), card.getKorName(), card.getImageUrl());
    }
}
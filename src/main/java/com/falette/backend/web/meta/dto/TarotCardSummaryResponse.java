package com.falette.backend.web.meta.dto;

import com.falette.backend.domain.card.TarotCard;

public record TarotCardSummaryResponse(Long cardId, String name, String imageUrl) {

    public static TarotCardSummaryResponse from(TarotCard card) {
        return new TarotCardSummaryResponse(card.getId(), card.getName(), card.getImageUrl());
    }
}
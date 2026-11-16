package com.falette.backend.web.question.dto;

import java.util.List;

/** 고민 유형별 질문 트리의 1차 질문 노드. 응답은 이 노드의 배열이다. */
public record ConcernQuestionTreeResponse(
        Long questionId,
        int questionLevel,
        String questionText,
        List<Option> options
) {
    public record Option(Long optionId, int optionOrder, String optionText, SubQuestion subQuestion) {
    }

    public record SubQuestion(Long questionId, int questionLevel, String questionText, List<SubOption> options) {
    }

    public record SubOption(Long optionId, int optionOrder, String optionText) {
    }
}
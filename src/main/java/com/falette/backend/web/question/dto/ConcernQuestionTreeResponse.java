package com.falette.backend.web.question.dto;

import com.falette.backend.domain.reading.ConcernType;

import java.util.List;

public record ConcernQuestionTreeResponse(
        ConcernType concernType,
        PrimaryQuestion primaryQuestion
) {
    public record PrimaryQuestion(Long id, String questionText, List<PrimaryOption> options) {
    }

    public record PrimaryOption(Long id, String optionText, SecondaryQuestion secondaryQuestion) {
    }

    public record SecondaryQuestion(Long id, String questionText, List<OptionItem> options) {
    }

    public record OptionItem(Long id, String optionText) {
    }
}
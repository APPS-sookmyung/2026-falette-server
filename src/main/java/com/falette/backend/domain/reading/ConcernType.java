package com.falette.backend.domain.reading;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ConcernType {
    LOVE("연애"),
    STUDY("학업"),
    CAREER("직업"),
    WEALTH("재물"),
    HEALTH("건강");

    private final String label;
}

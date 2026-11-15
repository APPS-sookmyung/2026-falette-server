package com.falette.backend.domain.reading;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ColorReason {
    INTUITIVE("직관적 끌림"),
    COMFORT("안정감과 위로"),
    ENERGY("에너지와 변화"),
    IDEAL("이상향과 동경"),
    PAST_MEMORY("과거의 기억");

    private final String label;
}

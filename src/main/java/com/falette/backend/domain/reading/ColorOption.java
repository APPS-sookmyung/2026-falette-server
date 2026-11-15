package com.falette.backend.domain.reading;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/** 색 9개. hex 값은 API 명세서 2-1 기준. */
@Getter
@RequiredArgsConstructor
public enum ColorOption {
    RED("빨간색", "#E53E3E"),
    ORANGE("주황색", "#ED8936"),
    YELLOW("노란색", "#ECC94B"),
    GREEN("초록색", "#38A169"),
    BLUE("파란색", "#3182CE"),
    NAVY("남색", "#2B6CB0"),
    PURPLE("보라색", "#805AD5"),
    WHITE("흰색", "#FFFFFF"),
    BLACK("검은색", "#1A202C");

    private final String label;
    private final String hex;
}

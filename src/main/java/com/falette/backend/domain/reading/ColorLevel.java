package com.falette.backend.domain.reading;

/**
 * 명도/채도 3단계 (API 명세서 기준 LEVEL_1~3). 디자인의 왼→오 순서와 같다.
 * 명도: LEVEL_1 = 가장 밝음, LEVEL_3 = 가장 어두움.
 * 채도: LEVEL_1 = 가장 선명, LEVEL_3 = 가장 탁함.
 */
public enum ColorLevel {
    LEVEL_1,
    LEVEL_2,
    LEVEL_3
}

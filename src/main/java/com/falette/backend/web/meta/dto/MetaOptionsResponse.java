package com.falette.backend.web.meta.dto;

import java.util.List;

public record MetaOptionsResponse(
        List<Concern> concerns,
        List<Color> colors,
        List<ColorReasonOption> colorReasons
) {
    public record Concern(String code, String label) {
    }

    public record Color(String code, String label, String hex) {
    }

    public record ColorReasonOption(String code, String label) {
    }

    /** 확정 명세서 2-1 기준 고정 데이터 */
    public static MetaOptionsResponse defaults() {
        return new MetaOptionsResponse(
                List.of(
                        new Concern("LOVE", "연애"),
                        new Concern("STUDY", "학업"),
                        new Concern("CAREER", "직업"),
                        new Concern("WEALTH", "재물"),
                        new Concern("HEALTH", "건강")),
                List.of(
                        new Color("RED", "빨간색", "#E53E3E"),
                        new Color("ORANGE", "주황색", "#ED8936"),
                        new Color("YELLOW", "노란색", "#ECC94B"),
                        new Color("GREEN", "초록색", "#38A169"),
                        new Color("BLUE", "파란색", "#3182CE"),
                        new Color("NAVY", "남색", "#2B6CB0"),
                        new Color("PURPLE", "보라색", "#805AD5"),
                        new Color("WHITE", "흰색", "#FFFFFF"),
                        new Color("BLACK", "검은색", "#1A202C")),
                List.of(
                        new ColorReasonOption("INTUITIVE", "직관적 끌림"),
                        new ColorReasonOption("COMFORT", "안정감과 위로"),
                        new ColorReasonOption("ENERGY", "에너지와 변화"),
                        new ColorReasonOption("IDEAL", "이상향과 동경"),
                        new ColorReasonOption("PAST_MEMORY", "과거의 기억")));
    }
}
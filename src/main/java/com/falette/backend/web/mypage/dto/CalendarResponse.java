package com.falette.backend.web.mypage.dto;

import com.falette.backend.domain.reading.ColorOption;

import java.time.LocalDate;
import java.util.List;

/** GET /api/v1/mypage/calendar 응답 (API 명세서 4-1) */
public record CalendarResponse(int year, int month, List<Record> records) {

    public record Record(Long readingId, LocalDate date, ColorOption color) {
    }
}

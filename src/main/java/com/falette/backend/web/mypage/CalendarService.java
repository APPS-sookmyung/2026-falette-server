package com.falette.backend.web.mypage;

import com.falette.backend.common.exception.BusinessException;
import com.falette.backend.common.exception.ErrorCode;
import com.falette.backend.domain.reading.Reading;
import com.falette.backend.domain.reading.ReadingRepository;
import com.falette.backend.domain.user.User;
import com.falette.backend.web.mypage.dto.CalendarResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalendarService {

    /** 날짜 구분 기준은 한국 시간 (UTC로 자르면 오전 9시 이전 기록이 전날로 찍힘) */
    private static final ZoneId ZONE = ZoneId.of("Asia/Seoul");

    private final ReadingRepository readingRepository;

    /**
     * 해당 월의 날짜별 기록.
     * 캘린더 한 칸에는 색 하나만 보이므로, 하루에 여러 번 했으면 그날 가장 마지막 리딩만 내려준다.
     */
    public CalendarResponse getMonthly(User user, int year, int month) {
        YearMonth yearMonth = toYearMonth(year, month);

        var from = yearMonth.atDay(1).atStartOfDay(ZONE).toInstant();
        var to = yearMonth.plusMonths(1).atDay(1).atStartOfDay(ZONE).toInstant();

        // 최신순으로 오므로 날짜별 첫 번째 = 그날의 마지막 리딩
        Map<LocalDate, Reading> latestByDate = new LinkedHashMap<>();
        for (Reading reading : readingRepository.findByUserInPeriod(user.getId(), from, to)) {
            LocalDate date = reading.getCreatedAt().atZone(ZONE).toLocalDate();
            latestByDate.putIfAbsent(date, reading);
        }

        List<CalendarResponse.Record> records = latestByDate.entrySet().stream()
                .map(e -> new CalendarResponse.Record(e.getValue().getId(), e.getKey(), e.getValue().getColor()))
                .sorted(Comparator.comparing(CalendarResponse.Record::date))
                .toList();

        return new CalendarResponse(year, month, records);
    }

    private YearMonth toYearMonth(int year, int month) {
        try {
            return YearMonth.of(year, month);
        } catch (DateTimeException e) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        }
    }
}

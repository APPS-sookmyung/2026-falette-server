package com.falette.backend.web.reading;

import com.falette.backend.auth.CurrentUser;
import com.falette.backend.domain.user.User;
import com.falette.backend.web.reading.dto.ReadingDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 리딩 조회 API (마이페이지 담당).
 * 리딩 생성 POST /api/v1/tarot/readings 는 리딩 플로우 담당이 별도 컨트롤러로 추가.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tarot/readings")
public class ReadingQueryController {

    private final ReadingQueryService readingQueryService;

    /** 결과 화면 새로고침 / 마이페이지 캘린더 날짜 클릭 팝업 공용 */
    @GetMapping("/{readingId}")
    public ReadingDetailResponse getReading(@PathVariable Long readingId, @CurrentUser User user) {
        return readingQueryService.getDetail(readingId, user);
    }
}

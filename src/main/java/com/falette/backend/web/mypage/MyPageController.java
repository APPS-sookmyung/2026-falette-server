package com.falette.backend.web.mypage;

import com.falette.backend.auth.CurrentUser;
import com.falette.backend.domain.user.User;
import com.falette.backend.web.mypage.dto.CalendarResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mypage")
public class MyPageController {

    private final CalendarService calendarService;

    /** 예) GET /api/v1/mypage/calendar?year=2026&month=9 */
    @GetMapping("/calendar")
    public CalendarResponse calendar(@CurrentUser User user,
                                     @RequestParam int year,
                                     @RequestParam int month) {
        return calendarService.getMonthly(user, year, month);
    }
}

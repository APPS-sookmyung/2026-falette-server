package com.falette.backend.web.reading;

import com.falette.backend.auth.CurrentUser;
import com.falette.backend.domain.user.User;
import com.falette.backend.web.reading.dto.ReadingCreateRequest;
import com.falette.backend.web.reading.dto.ReadingDetailResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/** 리딩 생성 API (리딩 플로우 담당). 조회는 ReadingQueryController. */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tarot/readings")
public class ReadingCommandController {

    private final ReadingCommandService readingCommandService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReadingDetailResponse create(@Valid @RequestBody ReadingCreateRequest request,
                                        @CurrentUser User user) {
        return readingCommandService.create(request, user);
    }
}
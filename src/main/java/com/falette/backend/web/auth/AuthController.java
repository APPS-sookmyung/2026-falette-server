package com.falette.backend.web.auth;

import com.falette.backend.auth.CurrentUser;
import com.falette.backend.auth.CurrentUserResolver;
import com.falette.backend.auth.GuestMergeService;
import com.falette.backend.common.exception.BusinessException;
import com.falette.backend.common.exception.ErrorCode;
import com.falette.backend.domain.user.User;
import com.falette.backend.web.auth.dto.GuestMergeResponse;
import com.falette.backend.web.auth.dto.MeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 인증 관련 API
 * - 구글 로그인 진입: GET /oauth2/authorization/google (Spring Security가 처리)
 * - 로그아웃: POST /api/v1/auth/logout (SecurityConfig의 logout 설정이 처리)
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final CurrentUserResolver currentUserResolver;
    private final GuestMergeService guestMergeService;

    /** 현재 사용자 상태. 세션도 X-Guest-UUID도 없으면 401 */
    @GetMapping("/me")
    public MeResponse me(@CurrentUser User user) {
        return MeResponse.from(user);
    }

    /**
     * 비회원 기록을 로그인한 구글 계정으로 합치기.
     * 구글 로그인 세션 필수 (없으면 401 LOGIN_REQUIRED). 헤더가 없으면 아무것도 안 하고 0.
     * 여러 번 호출해도 안전하다 (두 번째부터는 0).
     */
    @PostMapping("/merge-guest")
    public GuestMergeResponse mergeGuest(
            @RequestHeader(value = CurrentUserResolver.GUEST_HEADER, required = false) String guestUuid) {
        User loginUser = currentUserResolver.findLoginUser()
                .orElseThrow(() -> new BusinessException(ErrorCode.LOGIN_REQUIRED));
        if (guestUuid == null || guestUuid.isBlank()) {
            return new GuestMergeResponse(0);
        }
        int merged = guestMergeService.merge(loginUser, CurrentUserResolver.normalize(guestUuid));
        return new GuestMergeResponse(merged);
    }
}

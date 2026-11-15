package com.falette.backend.web.auth;

import com.falette.backend.auth.CurrentUser;
import com.falette.backend.domain.user.User;
import com.falette.backend.web.auth.dto.MeResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 인증 관련 API
 * - 구글 로그인 진입: GET /oauth2/authorization/google (Spring Security가 처리)
 * - 로그아웃: POST /api/v1/auth/logout (SecurityConfig의 logout 설정이 처리)
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    /** 현재 사용자 상태. 세션도 X-Guest-UUID도 없으면 401 */
    @GetMapping("/me")
    public MeResponse me(@CurrentUser User user) {
        return MeResponse.from(user);
    }
}

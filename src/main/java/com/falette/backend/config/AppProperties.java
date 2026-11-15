package com.falette.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * application.yml의 app.* 설정
 *
 * @param corsAllowedOrigins 프론트 주소 목록 (쿠키를 주고받으므로 * 불가)
 * @param loginSuccessRedirectUrl 구글 로그인 성공 후 돌아갈 프론트 주소
 * @param loginFailureRedirectUrl 구글 로그인 실패 시 돌아갈 프론트 주소
 */
@ConfigurationProperties(prefix = "app")
public record AppProperties(
        List<String> corsAllowedOrigins,
        String loginSuccessRedirectUrl,
        String loginFailureRedirectUrl
) {
}

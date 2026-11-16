package com.falette.backend.web.auth.dto;

import com.falette.backend.domain.user.AuthProvider;
import com.falette.backend.domain.user.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * GET /api/v1/auth/me 응답 (API 명세서 1-2)
 * - 구글: isAuthenticated, provider, userId, email, nickname
 * - 비회원: isAuthenticated, provider, guestUuid
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record MeResponse(
        @JsonProperty("isAuthenticated") boolean isAuthenticated,
        AuthProvider provider,
        Long userId,
        String email,
        String nickname,
        String guestUuid
) {
    public static MeResponse from(User user) {
        if (user.isGuest()) {
            return new MeResponse(false, AuthProvider.GUEST, null, null, null, user.getGuestUuid());
        }
        return new MeResponse(true, user.getProvider(), user.getId(), user.getEmail(), user.getNickname(), null);
    }
}

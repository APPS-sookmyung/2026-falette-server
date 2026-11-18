package com.falette.backend.auth;

import com.falette.backend.auth.oauth.FaletteOAuth2User;
import com.falette.backend.common.exception.BusinessException;
import com.falette.backend.common.exception.ErrorCode;
import com.falette.backend.domain.user.User;
import com.falette.backend.domain.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CurrentUserResolver {

    public static final String GUEST_HEADER = "X-Guest-UUID";

    private final UserRepository userRepository;
    private final GuestUserService guestUserService;

    /** 현재 사용자를 찾고, 없으면 401 */
    public User resolve(HttpServletRequest request) {
        return resolveOptional(request)
                .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED));
    }

    public Optional<User> resolveOptional(HttpServletRequest request) {
        // 1) 구글 로그인 세션 우선
        Optional<User> loginUser = findLoginUser();
        if (loginUser.isPresent()) {
            return loginUser;
        }

        // 2) 비회원 헤더
        String guestUuid = request.getHeader(GUEST_HEADER);
        if (guestUuid == null || guestUuid.isBlank()) {
            return Optional.empty();
        }
        return Optional.of(guestUserService.findOrCreate(normalize(guestUuid)));
    }

    /** 구글 로그인 세션의 사용자 (비회원 헤더는 보지 않음) */
    public Optional<User> findLoginUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof FaletteOAuth2User principal) {
            return userRepository.findById(principal.getUserId());
        }
        return Optional.empty();
    }

    /** UUID 형식 검사 + 소문자로 통일 */
    public static String normalize(String raw) {
        try {
            return UUID.fromString(raw.trim()).toString();
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.INVALID_GUEST_UUID);
        }
    }
}

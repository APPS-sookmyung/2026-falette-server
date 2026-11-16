package com.falette.backend.auth.oauth;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** 세션에 저장되는 로그인 정보. APP_USER의 id를 들고 있다. */
@Getter
public class FaletteOAuth2User implements OAuth2User, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Long userId;
    private final Map<String, Object> attributes;

    public FaletteOAuth2User(Long userId, Map<String, Object> attributes) {
        this.userId = userId;
        this.attributes = Collections.unmodifiableMap(new LinkedHashMap<>(attributes));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getName() {
        return String.valueOf(userId);
    }
}

package com.falette.backend.auth.oauth;

import com.falette.backend.domain.user.User;
import com.falette.backend.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 구글 로그인 성공 시 APP_USER에 가입(최초) 또는 프로필 갱신 */
@Service
@RequiredArgsConstructor
public class GoogleOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User googleUser = super.loadUser(userRequest);

        String googleId = googleUser.getAttribute("sub");
        String email = googleUser.getAttribute("email");
        String nickname = googleUser.getAttribute("name");

        User user = userRepository.findByGoogleId(googleId)
                .map(found -> {
                    found.updateGoogleProfile(email, nickname);
                    return found;
                })
                .orElseGet(() -> userRepository.save(User.google(googleId, email, nickname)));

        return new FaletteOAuth2User(user.getId(), googleUser.getAttributes());
    }
}

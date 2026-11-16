package com.falette.backend.config;

import com.falette.backend.auth.CurrentUserResolver;
import com.falette.backend.auth.oauth.GoogleOAuth2UserService;
import com.falette.backend.common.exception.ErrorCode;
import com.falette.backend.common.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 인증 구조 (API 명세서 "인증/식별 아키텍처 규칙" 기준)
 * - 구글 회원: Spring Security OAuth2 로그인 → JSESSIONID 세션 쿠키
 * - 비회원: X-Guest-UUID 헤더 (CurrentUserResolver에서 처리)
 * - 로그인/비회원 판별과 401 처리는 컨트롤러의 @CurrentUser에서 하므로 여기서는 전부 permitAll
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final AppProperties appProperties;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            ObjectProvider<ClientRegistrationRepository> clientRegistrations,
            GoogleOAuth2UserService googleOAuth2UserService) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                // 인증이 필요한데 없을 때 로그인 페이지로 리다이렉트하지 않고 401 JSON
                .exceptionHandling(ex -> ex.authenticationEntryPoint(
                        (request, response, e) -> writeJson(response, HttpServletResponse.SC_UNAUTHORIZED,
                                ErrorResponse.of(ErrorCode.UNAUTHORIZED))))
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/api/v1/auth/logout", HttpMethod.POST.name()))
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessHandler((request, response, auth) -> writeJson(response,
                                HttpServletResponse.SC_OK, Map.of("message", "로그아웃되었습니다."))));

        // 구글 client-id/secret이 설정된 경우에만 OAuth2 로그인 활성화
        // (설정이 없으면 로컬에서 비회원 흐름만으로도 서버가 뜨도록)
        if (clientRegistrations.getIfAvailable() != null) {
            http.oauth2Login(oauth -> oauth
                    .userInfoEndpoint(userInfo -> userInfo.userService(googleOAuth2UserService))
                    .successHandler((request, response, auth) ->
                            response.sendRedirect(appProperties.loginSuccessRedirectUrl()))
                    .failureHandler((request, response, e) ->
                            response.sendRedirect(appProperties.loginFailureRedirectUrl())));
        }

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(appProperties.corsAllowedOrigins());
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Content-Type", CurrentUserResolver.GUEST_HEADER));
        config.setAllowCredentials(true); // 세션 쿠키 전송 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    private void writeJson(HttpServletResponse response, int status, Object body) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), body);
    }
}

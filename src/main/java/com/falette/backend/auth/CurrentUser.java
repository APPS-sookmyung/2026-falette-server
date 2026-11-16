package com.falette.backend.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 컨트롤러 파라미터에 붙이면 현재 사용자(User)를 주입해 준다.
 * 순서: 구글 로그인 세션 → X-Guest-UUID 헤더 → 둘 다 없으면 401
 *
 * 예) public ResponseEntity<?> foo(@CurrentUser User user) { ... }
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentUser {
}

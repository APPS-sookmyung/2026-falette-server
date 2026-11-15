package com.falette.backend.domain.user;

import com.falette.backend.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * APP_USER 테이블. 비회원(GUEST)과 구글 회원(GOOGLE)을 한 테이블에서 관리한다.
 * (테이블명을 user로 하면 PostgreSQL 예약어와 겹쳐서 app_user 사용)
 */
@Getter
@Entity
@Table(name = "app_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AuthProvider provider;

    @Column(name = "guest_uuid", unique = true)
    private String guestUuid;

    @Column(name = "google_id", unique = true)
    private String googleId;

    @Column(unique = true)
    private String email;

    @Column(length = 50)
    private String nickname;

    public static User guest(String guestUuid) {
        User user = new User();
        user.provider = AuthProvider.GUEST;
        user.guestUuid = guestUuid;
        return user;
    }

    public static User google(String googleId, String email, String nickname) {
        User user = new User();
        user.provider = AuthProvider.GOOGLE;
        user.googleId = googleId;
        user.email = email;
        user.nickname = nickname;
        return user;
    }

    /** 구글 로그인 때마다 최신 프로필로 갱신 */
    public void updateGoogleProfile(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }

    public boolean isGuest() {
        return provider == AuthProvider.GUEST;
    }
}

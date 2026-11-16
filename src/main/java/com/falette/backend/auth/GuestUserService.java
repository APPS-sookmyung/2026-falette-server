package com.falette.backend.auth;

import com.falette.backend.domain.user.User;
import com.falette.backend.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

/**
 * 비회원은 프론트가 만든 UUID를 처음 보낼 때 자동으로 APP_USER에 등록한다.
 * (별도 POST /guest-users 없이, API 명세서의 X-Guest-UUID 헤더 방식)
 */
@Service
@RequiredArgsConstructor
public class GuestUserService {

    private final UserRepository userRepository;

    /**
     * 일부러 @Transactional을 걸지 않음: 저장이 중복 키로 실패해도
     * 그 트랜잭션만 롤백되고, 이어지는 재조회는 새 트랜잭션에서 정상 동작하게 하기 위함
     */
    public User findOrCreate(String guestUuid) {
        return userRepository.findByGuestUuid(guestUuid)
                .orElseGet(() -> create(guestUuid));
    }

    private User create(String guestUuid) {
        try {
            return userRepository.saveAndFlush(User.guest(guestUuid));
        } catch (DataIntegrityViolationException e) {
            // 같은 UUID로 요청이 동시에 두 번 들어온 경우: 먼저 만들어진 걸 사용
            return userRepository.findByGuestUuid(guestUuid).orElseThrow(() -> e);
        }
    }
}

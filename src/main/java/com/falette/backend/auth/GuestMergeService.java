package com.falette.backend.auth;

import com.falette.backend.domain.reading.ReadingRepository;
import com.falette.backend.domain.user.User;
import com.falette.backend.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 비회원으로 쓰던 기록을 구글 계정으로 옮긴다.
 * 구글 로그인 직후 프론트가 X-Guest-UUID 헤더와 함께 한 번 호출한다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GuestMergeService {

    private final UserRepository userRepository;
    private final ReadingRepository readingRepository;

    /**
     * @return 옮긴 리딩 개수. 해당 비회원이 없거나 이미 합쳐졌으면 0
     */
    @Transactional
    public int merge(User googleUser, String guestUuid) {
        return userRepository.findByGuestUuid(guestUuid)
                .filter(User::isGuest)
                .map(guest -> {
                    Long guestId = guest.getId();
                    int moved = readingRepository.reassignUser(guest, googleUser);
                    userRepository.deleteById(guestId);
                    log.info("비회원 {} 기록 {}개를 사용자 {}로 합침", guestId, moved, googleUser.getId());
                    return moved;
                })
                .orElse(0);
    }
}

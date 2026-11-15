package com.falette.backend.web.reading;

import com.falette.backend.common.exception.BusinessException;
import com.falette.backend.common.exception.ErrorCode;
import com.falette.backend.domain.reading.Reading;
import com.falette.backend.domain.reading.ReadingRepository;
import com.falette.backend.domain.user.User;
import com.falette.backend.web.reading.dto.ReadingDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadingQueryService {

    private final ReadingRepository readingRepository;

    /** 본인 기록만 조회 가능. 남의 기록이면 존재 여부를 숨기려고 404로 응답 */
    public ReadingDetailResponse getDetail(Long readingId, User user) {
        Reading reading = readingRepository.findWithCardById(readingId)
                .filter(r -> r.isOwnedBy(user))
                .orElseThrow(() -> new BusinessException(ErrorCode.READING_NOT_FOUND));
        return ReadingDetailResponse.from(reading);
    }
}

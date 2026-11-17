package com.falette.backend.web.reading;

import com.falette.backend.common.exception.BusinessException;
import com.falette.backend.common.exception.ErrorCode;
import com.falette.backend.domain.card.TarotCard;
import com.falette.backend.domain.card.TarotCardRepository;
import com.falette.backend.domain.question.ConcernQuestion;
import com.falette.backend.domain.question.ConcernQuestionOption;
import com.falette.backend.domain.question.ConcernQuestionOptionRepository;
import com.falette.backend.domain.reading.Reading;
import com.falette.backend.domain.reading.ReadingRepository;
import com.falette.backend.domain.user.User;
import com.falette.backend.web.reading.dto.ReadingCreateRequest;
import com.falette.backend.web.reading.dto.ReadingDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReadingCommandService {

    private final ReadingRepository readingRepository;
    private final TarotCardRepository tarotCardRepository;
    private final ConcernQuestionOptionRepository optionRepository;
    private final ReadingInterpreter interpreter;

    /** 리딩 생성: 입력 검증 → 목업 해석 생성 → 저장 → 상세 응답 */
    public ReadingDetailResponse create(ReadingCreateRequest request, User user) {
        TarotCard card = tarotCardRepository.findById(request.tarotCardId())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_TAROT_CARD));

        ConcernQuestionOption option1 = optionRepository.findById(request.concernOption1Id())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_CONCERN_OPTION));
        ConcernQuestionOption option2 = optionRepository.findById(request.concernOption2Id())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_CONCERN_OPTION));

        validateOptions(request, option1, option2);

        ReadingInterpreter.Result result = interpreter.generate(new ReadingInterpreter.Input(
                request.concernType().name(),
                request.color().name(),
                request.brightnessLevel().name(),
                request.saturationLevel().name(),
                request.colorReason().name(),
                card,
                option1.getOptionText(),
                option2.getOptionText()));

        Reading reading = Reading.create(
                user,
                request.concernType(),
                request.color(),
                request.brightnessLevel(),
                request.saturationLevel(),
                request.colorReason(),
                option1.getId(),
                option2.getId(),
                option1.getOptionText(),
                option2.getOptionText(),
                card,
                result.summary(),
                result.interpretation(),
                result.keywords(),
                result.luckyItem());

        readingRepository.save(reading);
        return ReadingDetailResponse.from(reading);
    }

    /** 1차 선택지는 해당 고민 유형의 1차 질문 소속, 2차 선택지는 그 1차 선택지에 딸린 2차 질문 소속이어야 한다 */
    private void validateOptions(ReadingCreateRequest request,
                                 ConcernQuestionOption option1, ConcernQuestionOption option2) {
        ConcernQuestion q1 = option1.getQuestion();
        ConcernQuestion q2 = option2.getQuestion();

        boolean option1Valid = q1.getQuestionOrder() == 1
                && q1.getConcernType() == request.concernType();
        boolean option2Valid = q2.getQuestionOrder() == 2
                && q2.getParentOption() != null
                && q2.getParentOption().getId().equals(option1.getId());

        if (!option1Valid || !option2Valid) {
            throw new BusinessException(ErrorCode.INVALID_CONCERN_OPTION);
        }
    }
}
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
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Service
@RequiredArgsConstructor
public class ReadingCommandService {

    private final ReadingRepository readingRepository;
    private final TarotCardRepository tarotCardRepository;
    private final ConcernQuestionOptionRepository optionRepository;
    private final ReadingInterpreter interpreter;
    private final PlatformTransactionManager transactionManager;

    /**
     * 리딩 생성: 입력 검증 → AI 해석 생성 → 저장 → 상세 응답.
     * AI 호출은 최악의 경우 1분 가까이 걸릴 수 있어서 트랜잭션 밖에서 하고,
     * 앞(검증·조회)과 뒤(저장)를 각각 짧은 트랜잭션으로 나눠 DB 커넥션을 오래 잡지 않는다.
     */
    public ReadingDetailResponse create(ReadingCreateRequest request, User user) {
        // 1. 검증과 입력 조회 (읽기 전용 트랜잭션)
        TransactionTemplate readTx = new TransactionTemplate(transactionManager);
        readTx.setReadOnly(true);
        Prepared prepared = readTx.execute(status -> prepare(request));

        // 2. AI 해석 생성 (트랜잭션 밖, DB 커넥션 없음)
        ReadingInterpreter.Result result = interpreter.generate(new ReadingInterpreter.Input(
                request.concernType().name(),
                request.color().name(),
                request.brightnessLevel().name(),
                request.saturationLevel().name(),
                request.colorReason().name(),
                prepared.card(),
                prepared.option1Text(),
                prepared.option2Text()));

        // 3. 저장 (쓰기 트랜잭션, 응답 변환까지 포함)
        TransactionTemplate writeTx = new TransactionTemplate(transactionManager);
        return writeTx.execute(status -> {
            Reading reading = Reading.create(
                    user,
                    request.concernType(),
                    request.color(),
                    request.brightnessLevel(),
                    request.saturationLevel(),
                    request.colorReason(),
                    prepared.option1Id(),
                    prepared.option2Id(),
                    prepared.option1Text(),
                    prepared.option2Text(),
                    prepared.card(),
                    result.summary(),
                    result.interpretation(),
                    result.keywords(),
                    result.luckyItem());

            readingRepository.save(reading);
            return ReadingDetailResponse.from(reading);
        });
    }

    private Prepared prepare(ReadingCreateRequest request) {
        TarotCard card = tarotCardRepository.findById(request.tarotCardId())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_TAROT_CARD));

        ConcernQuestionOption option1 = optionRepository.findById(request.concernOption1Id())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_CONCERN_OPTION));
        ConcernQuestionOption option2 = optionRepository.findById(request.concernOption2Id())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_CONCERN_OPTION));

        validateOptions(request, option1, option2);

        return new Prepared(card, option1.getId(), option2.getId(),
                option1.getOptionText(), option2.getOptionText());
    }

    /** 검증 트랜잭션에서 꺼낸 값만 담아 다음 단계로 넘기는 용도 (지연 로딩 엔티티는 넘기지 않음) */
    private record Prepared(TarotCard card, Long option1Id, Long option2Id,
                            String option1Text, String option2Text) {
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
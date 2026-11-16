package com.falette.backend.web.question;

import com.falette.backend.common.exception.BusinessException;
import com.falette.backend.common.exception.ErrorCode;
import com.falette.backend.domain.question.ConcernQuestion;
import com.falette.backend.domain.question.ConcernQuestionRepository;
import com.falette.backend.domain.reading.ConcernType;
import com.falette.backend.web.question.dto.ConcernQuestionTreeResponse;
import com.falette.backend.web.question.dto.ConcernQuestionTreeResponse.OptionItem;
import com.falette.backend.web.question.dto.ConcernQuestionTreeResponse.PrimaryOption;
import com.falette.backend.web.question.dto.ConcernQuestionTreeResponse.PrimaryQuestion;
import com.falette.backend.web.question.dto.ConcernQuestionTreeResponse.SecondaryQuestion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConcernQuestionService {

    private final ConcernQuestionRepository concernQuestionRepository;

    /** 고민 유형의 1차 질문 + 1차 선택지별 2차 질문 트리 */
    public ConcernQuestionTreeResponse getTree(ConcernType concernType) {
        ConcernQuestion primary = concernQuestionRepository
                .findByConcernTypeAndQuestionOrder(concernType, 1)
                .orElseThrow(() -> new BusinessException(ErrorCode.QUESTION_NOT_FOUND));

        Map<Long, ConcernQuestion> secondaryByParentOptionId = concernQuestionRepository
                .findAllByConcernTypeAndQuestionOrder(concernType, 2).stream()
                .collect(Collectors.toMap(q -> q.getParentOption().getId(), Function.identity()));

        var options = primary.getOptions().stream()
                .map(o -> new PrimaryOption(
                        o.getId(),
                        o.getOptionText(),
                        toSecondary(secondaryByParentOptionId.get(o.getId()))))
                .toList();

        return new ConcernQuestionTreeResponse(
                concernType,
                new PrimaryQuestion(primary.getId(), primary.getQuestionText(), options));
    }

    private SecondaryQuestion toSecondary(ConcernQuestion q) {
        if (q == null) {
            return null;
        }
        var options = q.getOptions().stream()
                .map(o -> new OptionItem(o.getId(), o.getOptionText()))
                .toList();
        return new SecondaryQuestion(q.getId(), q.getQuestionText(), options);
    }
}
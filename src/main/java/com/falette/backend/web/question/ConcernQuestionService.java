package com.falette.backend.web.question;

import com.falette.backend.common.exception.BusinessException;
import com.falette.backend.common.exception.ErrorCode;
import com.falette.backend.domain.question.ConcernQuestion;
import com.falette.backend.domain.question.ConcernQuestionRepository;
import com.falette.backend.domain.reading.ConcernType;
import com.falette.backend.web.question.dto.ConcernQuestionTreeResponse;
import com.falette.backend.web.question.dto.ConcernQuestionTreeResponse.Option;
import com.falette.backend.web.question.dto.ConcernQuestionTreeResponse.SubOption;
import com.falette.backend.web.question.dto.ConcernQuestionTreeResponse.SubQuestion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConcernQuestionService {

    private final ConcernQuestionRepository concernQuestionRepository;

    /** 고민 유형의 1차 질문 + 1차 선택지별 2차 질문 트리 */
    public List<ConcernQuestionTreeResponse> getTree(ConcernType concernType) {
        ConcernQuestion primary = concernQuestionRepository
                .findByConcernTypeAndQuestionOrder(concernType, 1)
                .orElseThrow(() -> new BusinessException(ErrorCode.QUESTION_NOT_FOUND));

        Map<Long, ConcernQuestion> secondaryByParentOptionId = concernQuestionRepository
                .findAllByConcernTypeAndQuestionOrder(concernType, 2).stream()
                .collect(Collectors.toMap(q -> q.getParentOption().getId(), Function.identity()));

        List<Option> options = primary.getOptions().stream()
                .map(o -> new Option(
                        o.getId(),
                        o.getOptionOrder(),
                        o.getOptionText(),
                        toSubQuestion(secondaryByParentOptionId.get(o.getId()))))
                .toList();

        return List.of(new ConcernQuestionTreeResponse(
                primary.getId(), primary.getQuestionOrder(), primary.getQuestionText(), options));
    }

    private SubQuestion toSubQuestion(ConcernQuestion q) {
        if (q == null) {
            return null;
        }
        List<SubOption> options = q.getOptions().stream()
                .map(o -> new SubOption(o.getId(), o.getOptionOrder(), o.getOptionText()))
                .toList();
        return new SubQuestion(q.getId(), q.getQuestionOrder(), q.getQuestionText(), options);
    }
}